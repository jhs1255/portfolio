import serial
import time

# 블루투스 시리얼 포트에 연결
bt = serial.Serial('/dev/rfcomm0', 9600)
time.sleep(2)  # 아두이노 초기화 대기

# 명령 전송
bt.write(b'humid on\n')
print("가습기 켬")

time.sleep(5)

bt.write(b'humid off\n')
print("가습기 끔")

bt.close()