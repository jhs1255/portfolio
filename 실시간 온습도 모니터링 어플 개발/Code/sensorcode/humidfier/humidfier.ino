// #include <SoftwareSerial.h>

// int Tx = 2;  //전송 보내는핀
// int Rx = 3;  //수신 받는핀

// int hum_pin = 5;

// SoftwareSerial BtSerial(Tx, Rx);

// void setup() {
//   // put your setup code here, to run once:
//   Serial.begin(9600);
//   BtSerial.begin(9600);
//   Serial.println("START");
//   pinMode(hum_pin, OUTPUT);
//   delay(2000);
// }

// void loop() {
//   // put your main code here, to run repeatedly:
//   if (BtSerial.available()) {
//     Serial.write(BtSerial.read());
//   }
//   if (Serial.available()) {
//     BtSerial.write(Serial.read());
//   }
//   digitalWrite(hum_pin, HIGH);
//   delay(3000);
//   digitalWrite(hum_pin, LOW);
//   delay(2000);
// }

#include <SoftwareSerial.h>

int Tx = 2;  // 블루투스 Tx
int Rx = 3;  // 블루투스 Rx

int hum_pin = 5;

SoftwareSerial BtSerial(Tx, Rx);

String inputString = "";    // 수신 문자열 저장용
bool humidOn = false;       // 가습기 상태

void setup() {
  Serial.begin(9600);
  BtSerial.begin(9600);
  Serial.println("START");
  pinMode(hum_pin, OUTPUT);
  digitalWrite(hum_pin, LOW);  // 초기에는 OFF
}

void loop() {
  // 블루투스로부터 문자열 수신
  while (BtSerial.available()) {
    char inChar = (char)BtSerial.read();
    inputString += inChar;

    // 줄바꿈 문자가 오면 명령 판단
    if (inChar == '\n') {
      inputString.trim(); // 앞뒤 공백 제거

      if (inputString == "humid on") {
        digitalWrite(hum_pin, HIGH);
        humidOn = true;
        Serial.println("Humidifier ON");
      } else if (inputString == "humid off") {
        digitalWrite(hum_pin, LOW);
        humidOn = false;
        Serial.println("Humidifier OFF");
      } else {
        Serial.println("Unknown command: " + inputString);
      }

      inputString = "";  // 문자열 초기화
    }
  }

  // PC -> 블루투스 전송 (필요하다면)
  if (Serial.available()) {
    BtSerial.write(Serial.read());
  }
}
