// 📁 lib/services/api_service.dart

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:pi_project/config/config.dart';

// ✅ 1. 측정소 목록 조회 (주소 기반)
Future<List<Map<String, dynamic>>> fetchStationList(String addr) async {
  final serviceKey = Config.wheatherAPIkey;
  final url =
      'http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getMsrstnList'
      '?addr=$addr&pageNo=1&numOfRows=100&returnType=json&serviceKey=$serviceKey';

  // ✅ 공공 API에서 허용된 시도 이름 리스트
  final validSidoNames = [
    '서울',
    '부산',
    '대구',
    '인천',
    '광주',
    '대전',
    '울산',
    '경기',
    '강원',
    '충북',
    '충남',
    '전북',
    '전남',
    '경북',
    '경남',
    '제주',
    '세종',
  ];

  try {
    final response = await http.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final jsonBody = json.decode(response.body);
      final items = jsonBody['response']['body']['items'];

      if (items != null) {
        return List<Map<String, dynamic>>.from(
          items
              .where(
                (item) => validSidoNames.contains(
                  item['addr'].toString().split(' ').first,
                ),
              )
              .map(
                (item) => {
                  'sidoName': item['addr'].toString().split(' ').first,
                  'stationName': item['stationName'] ?? '',
                },
              ),
        );
      } else {
        print("❌ 측정소 목록 없음");
      }
    } else {
      print("❌ 측정소 목록 API 오류: ${response.statusCode}");
    }
  } catch (e) {
    print("❌ 측정소 목록 API 호출 실패: $e");
  }
  return [];
}

//2
// ✅ 시도별 실시간 미세먼지 데이터 조회 후 stationName 기준 필터링
Future<Map<String, dynamic>?> fetchRealtimeDustData({
  required String sidoName,
  required String stationName,
}) async {
  final serviceKey = Config.wheatherAPIkey;
  final url =
      'http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty'
      '?sidoName=$sidoName&pageNo=1&numOfRows=100&returnType=json&serviceKey=$serviceKey&ver=1.0';

  try {
    final response = await http.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final jsonBody = json.decode(response.body);
      final items = jsonBody['response']['body']['items'];

      // 🔍 stationName 필터링
      final matched = items.firstWhere(
        (item) => item['stationName'] == stationName,
        orElse: () => null,
      );

      if (matched != null) {
        return {
          'stationName': matched['stationName'],
          'pm10Value': matched['pm10Value'],
          'pm25Value': matched['pm25Value'],
          'dataTime': matched['dataTime'],
        };
      } else {
        print('❗ 해당 stationName($stationName) 없음');
      }
    } else {
      print('❌ 실시간 미세먼지 API 오류: ${response.statusCode}');
    }
  } catch (e) {
    print('❌ 실시간 미세먼지 API 실패: $e');
  }

  return null;
}

// ✅ 오늘/내일/모레 미세먼지 예보 조회 (이미지 포함 버전)
Future<List<Map<String, dynamic>>> fetchDailyForecast({
  required String searchDate,
  String informCode = 'PM10',
}) async {
  final serviceKey = Config.wheatherAPIkey;

  final url =
      'http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth'
      '?searchDate=$searchDate'
      '&informCode=$informCode'
      '&ver=1.1'
      '&returnType=json'
      '&pageNo=1'
      '&numOfRows=100'
      '&serviceKey=$serviceKey';

  try {
    final response = await http.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final jsonBody = json.decode(response.body);
      final items = jsonBody['response']['body']['items'];
      if (items != null && items.isNotEmpty) {
        return List<Map<String, dynamic>>.from(items);
      } else {
        print("❌ 예보 데이터 없음 (items == null)");
      }
    } else {
      print("❌ 예보 API 오류: ${response.statusCode}");
    }
  } catch (e) {
    print("❌ 예보 API 호출 실패: $e");
  }
  return [];
}

// 📌 파일 맨 아래 아무 곳에
// 🔴 추가: informGrade 문자열(“충북 : 보통, …”)에서 원하는 시/도 등급 추출
const _alias = {
  '충남': '충청남도',
  '대전광역시': '충청남도',
  '충북': '충청북도',
  '경남': '경상남도',
  '경북': '경상북도',
  '전남': '전라남도',
  '전북': '전라북도',
};

String extractZoneGrade(String informGrade, String zone) {
  final target = _alias[zone.trim()] ?? zone.trim();
  final pattern = RegExp(r'([^,:]+)\s*:\s*([^,]+)');
  for (final m in pattern.allMatches(informGrade)) {
    final region = m.group(1)!.trim();
    if (region == target || region.startsWith(zone.trim())) {
      return m.group(2)?.trim() ?? '정보 없음';
    }
  }
  return '정보 없음';
}
