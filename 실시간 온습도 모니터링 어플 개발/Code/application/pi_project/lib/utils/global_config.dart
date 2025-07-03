// global_config.dart
// 앱 전역에서 사용하는 공통 상태 및 상수 정의
//여기에 다 모아놨음.
import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:math';

import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import '../config/config.dart';

// =================== 🌍 지역 정보 (API) =====================
String zone = '충북';     // API에서 받아올 시/도명
String city = '용암동';     // API에서 받아올 측정소명

WebSocketChannel? wsChannel;
// 웹소켓을 사용하여 실시간으로 불러온 값을 저장하는 함수
void connectToSensorWebSocket(){
  const url = 'ws://${Config.baseURL}/flutter';
  wsChannel = WebSocketChannel.connect(Uri.parse(url));
  print("WebSocket 연결됨: $url");
  wsChannel!.stream.listen(
        (message) {
      print("수신된 센서 데이터: $message");
      updateSensorDataFromWebSocket(message); // 전역 상태 갱신
    },
    onError: (error) {
      print("WebSocket 오류 발생: $error");
    },
    onDone: () {
      print("WebSocket 연결 종료됨");
    },
  );
}
//웹소켓을 통해 불러온 값 넣기
void updateSensorDataFromWebSocket(String jsonMessage) {
  try {
    final Map<String, dynamic> data = jsonDecode(jsonMessage);

    if (data.containsKey('temp') &&
        data.containsKey('humi') &&
        data.containsKey('pm25') &&
        data.containsKey('pm10')) {

      double? temp = double.tryParse(data['temp'].toString());
      double? humi = double.tryParse(data['humi'].toString());
      int? pm25 = int.tryParse(data['pm25'].toString());
      int? pm10 = int.tryParse(data['pm10'].toString());

      if (temp != null && humi != null && pm25 != null && pm10 != null) {
        temperature.value = temp;
        humidity.value = humi;
        homePm25.value = pm25;
        homePm10.value = pm10;

        print("WebSocket 센서 데이터 반영 완료");
      } else {
        print("수신된 데이터 변환 실패: $jsonMessage");
      }
    } else {
      print("JSON에 필수 키 없음: $jsonMessage");
    }
  } catch (e) {
    print("WebSocket 데이터 파싱 오류: $e");
  }
}


// 블루투스를 사용하여 실시간으로 불러온 값을 저장하는 함수
void listenToCharacteristic(BluetoothCharacteristic characteristic) {
  characteristic.value.listen((value) {
    String receivedData = utf8.decode(value); // notify 받은 값 디코딩
    print("실시간 notify 값: $receivedData");

    List<String> data = receivedData.split(' ');

    // data 길이가 예상보다 적은 경우 예외 처리
    if (data.length >= 4) {
      temperature.value = (double.tryParse(data[0].split(':')[1])) ?? 0; // 온도
      humidity.value = (double.tryParse(data[1].split(':')[1])) ?? 0;   // 습도
      homePm25.value = (int.tryParse(data[2].split(':')[1]) ?? 0); // PM2.5
      homePm10.value = (int.tryParse(data[3].split(':')[1]) ?? 0); // PM10
      print("데이터 갱신 완료");
    } else {
      print("수신된 데이터 형식이 올바르지 않습니다: $receivedData");
    }
  });
}

// =================== 💨 미세먼지 수치 =======================
// 외부: API, 내부: 라즈베리파이 센서에서 수집
//테스트확인값 HOME
ValueNotifier<int> homePm10 = ValueNotifier(30);         // 집 내부 미세먼지 (PM10) - 라즈베리파이 센서
ValueNotifier<int> homePm25 = ValueNotifier(50);         // 집 내부 초미세먼지 (PM2.5) - 라즈베리파이 센서

// =================== 🌡 환경 상태 (센서) ====================
ValueNotifier<double> humidity = ValueNotifier(30);        // 실내 습도 - 센서
ValueNotifier<double> temperature = ValueNotifier(20); // 실내 온도 - 센서
//home_screen파일에 테스트용있음. 22번째줄
// =================== 🏡 사용자 설정 =========================
String homeName = '우리집';   // 사용자가 지정하는 이름 (SharedPreferences에 저장됨)

// =================== 📅 날짜 포맷 (현재 시각) ===============
String get formattedDate =>
    DateTime.now().toLocal().toString().substring(0, 16); // "yyyy-MM-dd HH:mm"

// =================== 🌤 예보 및 이미지 (API 예정) ===========
final Map<String, String> dummyForecastData = {
  "frcstOneDt": "2024-04-21 11:00",
  "frcstTwoDt": "2024-04-22 11:00",
  "frcstThreeDt": "2024-04-23 11:00",
  "frcstFourDt": "2024-04-24 11:00",
  "frcstOneCn": "서울: 좋음, 인천: 보통, 충북: 나쁨, 부산: 좋음",
  "frcstTwoCn": "서울: 보통, 인천: 좋음, 충북: 좋음, 부산: 나쁨",
  "frcstThreeCn": "서울: 나쁨, 인천: 좋음, 충북: 보통, 부산: 나쁨",
  "frcstFourCn": "서울: 좋음, 인천: 좋음, 충북: 좋음, 부산: 나쁨",
};

final String imageUrl7 = 'splashlogo.png';              // API에서 받아올 슬라이드 이미지1
final String imageUrl8 = 'https://example.com/image8.png'; // API에서 받아올 슬라이드 이미지2


// =================== 🗺 지역 리스트 (추후 API 대체 가능) ===
final List<Map<String, String>> allRegionData = [
  {'sidoName': '충청북도', 'stationName': '분평동'},
  {'sidoName': '천안시', 'stationName': '동남구'},
  {'sidoName': '충청북도', 'stationName': '오창읍'},
  {'sidoName': '대전광역시', 'stationName': '유성구'},
  {'sidoName': '부산광역시' , 'stationName': '해운대구'},
  {'sidoName': '서울특별시', 'stationName': '강남구'},
  {'sidoName': '경기도', 'stationName': '수원시 영통구'},
  {'sidoName': '충청남도', 'stationName': '천안시 서북구'},
];