# 동기/비동기식 구현을 위한 라이브러리
import asyncio
# 웹소켓 통신을 위한 라이브러리
import websockets
# 입력받은 작업을 실행하기 위한 라이브러리
import subprocess
# json으로 값을 주고 받기 위한 라이브러리
import json
# thread를 사용하여 작업을 처리하기 위한 라이브러리
import threading
#시리얼 포트를 사용하여 아두이노와 통신하기 위한 라이브러리
import serial
# 일정 시간 간격을 주기 위한 라이브러리
import time

sensor_data_queue = asyncio.Queue()

# Websocket 서버 URL springboot 서버의 IP와 포트번호
WEB_SOCKET_SERVER = "ws://192.168.20.176:9988/raspberry"

# 실행 중인 프로세스를 추적할 변수
processes = {}

# 전역 프로세스 객체 저장용
temp_process = None
pms_process = None

# 센서 값 저장
latest_data = {
    "temp": None,
    "humi": None,
    "pm25": None,
    "pm10": None
}

# 미세먼지 값 갱신용 상태 저장 (최근 pm2.5, pm10 값 따로 보관)
pms_latest = {"pm25": None, "pm10": None}

main_loop = None
sensor_data_queue = None

def notify_combined_if_ready():
    global main_loop, sensor_data_queue

    print("디버깅: 최신 센서 데이터 확인")
    print(f"온도: {latest_data['temp']}, 습도: {latest_data['humi']}, PM2.5: {latest_data['pm25']}, PM10: {latest_data['pm10']}")
    # 모든 값이 존재하는지 확인
    if latest_data["temp"] and latest_data["humi"] and latest_data["pm25"] and latest_data["pm10"]:
        print("모든 데이터가 준비되었습니다.")

        if main_loop is None or sensor_data_queue is None:
            print("루프 또는 큐가 아직 설정되지 않았습니다. 재시도 대기 중...")
            return

        # 서버로 보낼 데이터 준비
        try:
            data_to_send = {
                "temp": latest_data["temp"],
                "humi": latest_data["humi"],
                "pm25": latest_data["pm25"],
                "pm10": latest_data["pm10"]
            }
            json_data = json.dumps(data_to_send)

            if main_loop is not None and sensor_data_queue is not None:
                def enqueue():
                    asyncio.run_coroutine_threadsafe(
                        sensor_data_queue.put(json_data),
                        main_loop
                    )
                main_loop.call_soon_threadsafe(enqueue)
            else:
                print("이벤트 루프 또는 큐가 아직 초기화되지 않았습니다.")
        except Exception as e:
            print(f"데이터 전송 중 오류 발생: {e}")
    else:
        print("데이터가 준비되지 않았습니다. 일부 값이 누락됨.")

# 온도, 습도 데이터를 파싱하는 함수
def parse_temp_line(line_str):
    try:
        parts = line_str.split(',')
        temp_part = parts[0].split('=')[1].replace('°C', '').strip()
        humi_part = parts[1].split('=')[1].replace('%', '').strip()
        return temp_part, humi_part
    except Exception as e:
        print(f"temp.py 파싱 에러: {e}")
        return None, None

# 미세먼지 데이터를 파싱하는 함수
def parse_pms_line(line_str):
    global pms_latest
    try:
        if "PM 2.5" in line_str:
            parts = line_str.split("PM 2.5 :")
            if len(parts) > 1:
                pm25_str = parts[1].strip().split(' ')[0]
                pms_latest["pm25"] = pm25_str
        if "PM 10.0" in line_str:
            parts = line_str.split("PM 10.0 :")
            if len(parts) > 1:
                pm10_str = parts[1].strip().split(' ')[0]
                pms_latest["pm10"] = pm10_str

        # pm25, pm10 모두 값이 있으면 반환
        if pms_latest["pm25"] is not None and pms_latest["pm10"] is not None:
            return pms_latest["pm25"], pms_latest["pm10"]
        else:
            return None, None
    except Exception as e:
        print(f"PMS7003.py 파싱 에러: {e}")
        return None, None

# 출력 스트리밍 함수 (비동기 방식)
def stream_output(process, name):
    def stream():
        try:
            for line in iter(process.stdout.readline, ''):
                line_str = line.strip()
                print(f"[{name}] {line_str}")  # 실시간 출력

                if name == "temp.py" and "온도=" in line_str and "습도=" in line_str:
                    temp, humi = parse_temp_line(line_str)
                    if temp and humi:
                        latest_data["temp"] = temp
                        latest_data["humi"] = humi
                        notify_combined_if_ready()  # 데이터가 준비되었는지 확인

                elif name == "PMS7003.py":
                    parse_pms_line(line_str)
                    if "CHKSUM result" in line_str:
                        pm25 = pms_latest.get("pm25")
                        pm10 = pms_latest.get("pm10")
                        if pm25 and pm10:
                            latest_data["pm25"] = pm25
                            latest_data["pm10"] = pm10
                            notify_combined_if_ready()  # 데이터가 준비되었는지 확인

        except Exception as e:
            print(f"{name} 출력 읽기 중 오류 발생: {e}")
    threading.Thread(target=stream, daemon=True).start()
    
# 예시로 온습도 센서 실행 함수 (동기화 방식)
def run_temp():
    global temp_process
    try:
        temp_process = subprocess.Popen(['python', '-u', 'temp.py'], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        print("temp.py 실행됨")
        stream_output(temp_process, "temp.py")  # 비동기적으로 출력 처리
    except Exception as e:
        print(f"temp.py 실행 중 오류 발생: {e}")

# 예시로 미세먼지 센서 실행 함수 (동기화 방식)
def run_pms():
    global pms_process
    try:
        pms_process = subprocess.Popen(['python', '-u', 'PMS7003.py'], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        print("PMS7003.py 실행됨")
        stream_output(pms_process, "PMS7003.py")  # 비동기적으로 출력 처리
    except Exception as e:
        print(f"PMS7003.py 실행 중 오류 발생: {e}")
        
# 프로세스를 종료하는 함수
def terminate_process(process, name):
    try:
        if process and process.poll() is None:  # 프로세스가 실행 중일 때만 종료
            process.terminate()
            print(f"{name} 종료 신호 보냄")
            process.wait(timeout=3)
            print(f"{name} 종료됨")
        else:
            print(f"{name} 실행 중이 아님")
    except subprocess.TimeoutExpired:
        print(f"{name} 종료 대기 시간 초과")
    except Exception as e:
        print(f"{name} 종료 중 오류 발생: {e}")

# 프로세스 변수 초기화 함수
def reset_process():
    global temp_process, pms_process
    temp_process = None
    pms_process = None
    print("프로세스 초기화 완료")

# 서버로 부터 메세지를 받고 작업을 하기 위한 함수
async def handle_command(device, command):
    try:
        # 명령 처리 예시 (실제로는 라즈베리파이에서 처리 후 결과를 반환)
        print(f"Device: {device}, Command: {command}")
        if command == "on":
            if device == "sensor":
                threading.Thread(target=run_temp, daemon=True).start()
                threading.Thread(target=run_pms, daemon=True).start()
                return f"{device}이(가) 켜졌습니다."
            elif device == "humidifier":
                #threading.Thread(target=execute_script, args=("humidifier.py",)).start()
                # 블루투스 시리얼 포트에 연결
                bt = serial.Serial('/dev/rfcomm0', 9600)
                time.sleep(1)  # 아두이노 초기화 대기

                # 명령 전송
                bt.write(b'humid on\n')
                print("가습기 켬")
                bt.close()
                return f"{device}이(가) 켜졌습니다."
            else:
                return "알 수 없는 장치입니다."

        elif command == "off":
            if device == "sensor":
                terminate_process(temp_process, "temp.py")
                terminate_process(pms_process, "PMS7003.py")
                reset_process()
                return f"{device}이(가) 꺼졌습니다."
            elif device == "humidifier":
                #terminate_process("humidifier.py")
                #reset_process()
                # 블루투스 시리얼 포트에 연결
                bt = serial.Serial('/dev/rfcomm0', 9600)
                time.sleep(2)  # 아두이노 초기화 대기
                bt.write(b'humid off\n')
                print("가습기 끔")

                bt.close()
                return f"{device}이(가) 꺼졌습니다."
            else:
                return "알 수 없는 장치입니다."

        else:
            return "잘못된 명령입니다."

    except Exception as e:
        print(f"명령 처리 중 오류 발생: {e}")
        return "명령 처리 오류 발생"
    
# WebSocket 클라이언트로 명령을 서버로 전송하고 응답을 받는 함수
async def send_command_to_server():
    print("시작됨")
    try:
        uri = WEB_SOCKET_SERVER
        async with websockets.connect(uri) as websocket:

            async def send_from_queue():
                while True:
                    try:
                        message = await sensor_data_queue.get()
                        await websocket.send(message)
                        print(f"서버로 센서 데이터 전송: {message}")
                    except Exception as e:
                        print(f"센서 데이터 전송 실패: {e}")
                        break

            # 병렬로 명령 수신과 센서 데이터 전송을 처리
            consumer_task = asyncio.create_task(send_from_queue())

            # 명령을 서버로부터 실시간으로 받기
            while True:
                try:
                    message = await websocket.recv()  # 서버로부터 메시지 수신
                    print(f"서버로부터 수신: {message}")

                    # 받은 메시지를 JSON으로 파싱
                    data = json.loads(message)
                    device = data.get("device")  # 장치 이름
                    command = data.get("command")  # 명령

                    # 명령 처리
                    result = await handle_command(device, command)

                    # 처리 결과를 Spring Boot 서버에 응답 (웹소켓을 통해)
                    response = json.dumps({"device": device, "result": result})
                    await websocket.send(response)
                    print(f"서버로 응답: {response}")

                except websockets.exceptions.ConnectionClosed as e:
                    print(f"웹소켓 연결이 종료되었습니다: {e}")
                    break
                except json.JSONDecodeError as e:
                    print(f"받은 메시지 JSON 파싱 오류: {e}")
                    continue
                except Exception as e:
                    print(f"기타 오류 발생: {e}")
                    break
            consumer_task.cancel()
    except websockets.exceptions.InvalidURI as e:
        print(f"WebSocket URI 오류: {e}")
    except Exception as e:
        print(f"WebSocket 연결 중 오류 발생: {e}")
    print("종료되었습니다.")

async def main():
    global main_loop, sensor_data_queue
    main_loop = asyncio.get_running_loop()
    sensor_data_queue = asyncio.Queue()  # ← 반드시 main 루프 안에서 생성되어야 함
    print("메인 루프 및 큐 초기화 완료")
    await send_command_to_server()
    #try:
        #await send_command_to_server()
    #except KeyboardInterrupt:
        #print("종료되었습니다.")

# asyncio를 사용하여 비동기 실행
asyncio.run(main())