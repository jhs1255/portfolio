from bluezero import peripheral, advertisement
import threading
import subprocess
import time
import os
import serial
#import uuid

# 어댑터 주소 (hciconfig로 확인한 값 넣기)
ADAPTER_ADDRESS = 'D8:3A:DD:77:E3:B2'  # ← 여기에 실제 값 넣으세요

# 랜덤 UUID 생성
#SERVICE_UUID = str(uuid.uuid4())
#CHAR_UUID = str(uuid.uuid4())

#고정 UUID 생성
SERVICE_UUID = "7a78bbc9-28bb-447a-bdce-813bc85abe24"
CHAR_UUID = "4b9989fa-990d-4231-897f-c7b703249ef9"


print(f"Service UUID: {SERVICE_UUID}")
print(f"Characteristic UUID: {CHAR_UUID}")


# 명령을 주고 받을 write/read용
char_value = bytearray("init", 'utf-8')


#전역 프로세스 객체 저장용
temp_process = None
pms_process = None

# BLE peripheral 전역 변수 (flutter 전송을 위한 notify용 characteristic 저장용도)
ble = None

# 센서 값 저장
latest_data = {
    "temp": None,
    "humi": None,
    "pm25": None,
    "pm10": None
}

# 미세먼지 값 갱신용 상태 저장 (최근 pm2.5, pm10 값 따로 보관)
pms_latest = {"pm25": None, "pm10": None}
# notify 간격
NOTIFY_INTERVAL = 2  # 초
last_notify_time = 0


def notify_combined_if_ready():
    global last_notify_time

    now = time.time()
    if now - last_notify_time >= NOTIFY_INTERVAL:
        temp = latest_data["temp"]
        humi = latest_data["humi"]
        pm25 = latest_data["pm25"]
        pm10 = latest_data["pm10"]

        if None not in (temp, humi, pm25, pm10):
            combined_value = bytearray(f"temp:{temp} humi:{humi} pm2.5:{pm25} pm10:{pm10}", 'utf-8')
            print(f"[Notify] 통합 전송 -> {combined_value.decode()}")
            if ble.characteristics:
                ble.characteristics[0].set_value(list(combined_value))
                ble.characteristics[0].notify = True

            last_notify_time = now

def parse_temp_line(line_str):
    # 예: "온도=24.5°C, 습도=57.0%"
    try:
        parts = line_str.split(',')
        temp_part = parts[0].split('=')[1].replace('°C','').strip()
        humi_part = parts[1].split('=')[1].replace('%','').strip()
        return temp_part, humi_part
    except Exception as e:
        print(f"temp.py 파싱 에러: {e}")
        return None, None

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

def stream_output(process, name):
    def stream():
        try:
            for line in iter(process.stdout.readline, ''):
                line_str = line.strip()
                print(f"[{name}] {line_str}")

                if name == "temp.py" and "온도=" in line_str and "습도=" in line_str:
                    temp, humi = parse_temp_line(line_str)
                    if temp and humi:
                        latest_data["temp"] = temp
                        latest_data["humi"] = humi
                        notify_combined_if_ready()

                elif name == "PMS7003.py":
                    parse_pms_line(line_str)
                    if "CHKSUM result" in line_str:
                        pm25 = pms_latest.get("pm25")
                        pm10 = pms_latest.get("pm10")
                        if pm25 and pm10:
                            latest_data["pm25"] = pm25
                            latest_data["pm10"] = pm10
                            notify_combined_if_ready()

        except Exception as e:
            print(f"{name} 출력 읽기 중 오류 발생: {e}")
    threading.Thread(target=stream, daemon=True).start()


# 온습도 센서 실행 함수
def run_temp():
    global temp_process
    try:
        temp_process = subprocess.Popen(['python','-u','temp.py'],stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True)
        print("temp.py 실행됨")
        stream_output(temp_process, "temp.py")
    except Exception as e:
        print(f"temp.py 실행 중 오류 발생: {e}")

#미세먼지 센서 실행 함수
def run_pms():
    global pms_process
    try:
        pms_process = subprocess.Popen(['python','-u','PMS7003.py'],stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True)
        print("PMS7003.py 실행됨")
        stream_output(pms_process, "PMS7003.py")
    except Exception as e:
        print(f"PMS7003.py 실행 중 오류 발생: {e}")

# 센서종료 함수
def terminate_process(process, name):
    try:
        if process and process.poll() is None:
            process.terminate()
            print(f"{name} 종료 신호 보냄")
            process.wait(timeout=3)
            print(f"{name} 종료됨")
    except subprocess.TimeoutExpired:
        print(f"{name} 종료 대기 시간 초과")
    except Exception as e:
        print(f"{name} 종료 중 오류 발생: {e}")

# 프로세스 변수 초기화 함수
def reset_process():
    global pms_process, temp_process
    temp_process = None
    pms_process = None


# 플러터에서 read 요청 시 호출되는 콜백 함수
def read_callback():
    print("Read request received")
    return list(char_value)

#플러터에서 write 요청 시 호출되는 콜백 함수
def write_callback(value, options):
    global char_value
    char_value = bytes(value)
    decoded = char_value.decode('utf-8').strip()
    print(f"Write request reeived: {decoded}")

    if decoded == "sensor : on":
        threading.Thread(target=run_temp, daemon=True).start()
        threading.Thread(target=run_pms, daemon=True).start()

    elif decoded == "sensor : off":
        terminate_process(temp_process, 'temp.py')
        terminate_process(pms_process, 'PMS7003.py')
        reset_process()

    elif decoded == "humidifier : on" :
        # 블루투스 시리얼 포트에 연결
        bt = serial.Serial('/dev/rfcomm0', 9600)
        time.sleep(1)  # 아두이노 초기화 대기
        # 명령 전송
        bt.write(b'humid on\n')
        print("가습기 켬")
        bt.close()

    elif decoded == "humidifier : off":
         # 블루투스 시리얼 포트에 연결
        bt = serial.Serial('/dev/rfcomm0', 9600)
        time.sleep(1)  # 아두이노 초기화 대기
        bt.write(b'humid off\n')
        print("가습기 끔")
        bt.close()

# adapter_address 추가
ble = peripheral.Peripheral(adapter_address=ADAPTER_ADDRESS, local_name='hsk91')

ble.add_service(srv_id=1, uuid=SERVICE_UUID, primary=True)
ble.add_characteristic(srv_id=1,
                       chr_id=1,
                       uuid=CHAR_UUID,
                       value=list(char_value),
                       notifying=True,
                       flags=['read', 'write','notify'],
                       read_callback=read_callback,
                       write_callback=write_callback)


# 광고에 UUID 포함 (블루투스와 플러터 통신 시 동적으로 UUID값을 주고 받기 위해 필요)
advert_id = 0
adv = advertisement.Advertisement(advert_id, ADAPTER_ADDRESS)
adv.service_UUIDs = [SERVICE_UUID]
adv.local_name = 'hsk91'

ble.advertisement = adv

ble.publish()

print("BLE GATT 서버 실행 중... Ctrl+C로 종료하세요.")
