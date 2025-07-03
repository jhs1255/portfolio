import time
import board
import adafruit_dht
import RPi.GPIO as GPIO
import signal
import sys

dht_device = adafruit_dht.DHT11(board.D3)

def clean_exit(sig=None, frame=None):
    try:
        if dht_device:
            dht_device.exit()
    except Exception as e:
        if "remove(x): x not in list" not in str(e):
            print("[temp.py] dht_device 종료 중 오류:", e)
    GPIO.cleanup()
    sys.exit(0)

signal.signal(signal.SIGINT, clean_exit)
signal.signal(signal.SIGTERM, clean_exit)


# 현재 실행 폴더에 파일 생성
#file_path = os.path.join(os.getcwd(), "temp_data.json")

try:
    while True:
        try:
            temp = dht_device.temperature
            humid = dht_device.humidity
#            if temp is not None and humid is not None:
#               data = {"TEMP": temp, "HUMI": humid}
                # 파일 덮어쓰기
#                with open(file_path, "w") as f:
#                    json.dump(data, f)

#                print('온도={0:0.1f}°C, 습도={1:0.1f}%'.format(temp, humid))
#            else:
#               print("값을 읽을 수 없습니다.")

            if temp is not None and humid is not None:
                print('온도={0:0.1f}°C, 습도={1:0.1f}%'.format(temp, humid))
            else:
                print("값을 읽을 수 없습니다.")

        except RuntimeError as e:
            print("측정 오류:", e)

        time.sleep(2)  # 최소 2초 간격 권장

except KeyboardInterrupt:
    print("사용자에 의해 종료되었습니다.")
finally:
    clean_exit()