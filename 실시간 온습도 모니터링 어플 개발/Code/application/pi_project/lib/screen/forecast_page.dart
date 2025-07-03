import 'package:flutter/material.dart';
import 'package:pi_project/widgets/image_slider.dart';
import 'package:pi_project/widgets/forecast_card.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/services/api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ForecastPage extends StatefulWidget {
  final String zone; // 🔴 시/도명 (예: 충북)
  const ForecastPage({Key? key, required this.zone}) : super(key: key);

  @override
  State<ForecastPage> createState() => _ForecastPageState();
}

class _ForecastPageState extends State<ForecastPage> {
  List<Map<String, dynamic>> _forecastList = [];
  String? _savedZone;

  @override
  void initState() {
    super.initState();
    // _loadForecast();
    _loadZoneFromPrefsAndForecast();
  }

  Future<void> _loadZoneFromPrefsAndForecast() async {
    final prefs = await SharedPreferences.getInstance();
    final savedZone = prefs.getString('recentZone') ?? widget.zone; // 저장된 zone 불러오기

    setState(() {
      _savedZone = savedZone; // 저장된 zone을 상태 변수에 저장
    });

    final now = DateTime.now();
    final futures = List.generate(3, (i) {
      final d = now.add(Duration(days: i));
      final y = d.year, m = d.month.toString().padLeft(2, '0'), dd = d.day.toString().padLeft(2, '0');
      return fetchDailyForecast(searchDate: "$y-$m-$dd");
    });

    final results = (await Future.wait(futures)).expand((e) => e).toList();
    final filtered = results
        .where((e) => e['informCode'] == 'PM10')
        .toList()
      ..sort((a, b) => (a['informData'] as String).compareTo(b['informData']));

    setState(() => _forecastList = filtered);
  }


  Future<void> _loadForecast() async {
    final now = DateTime.now();
        /// 오늘‧내일‧모레(3 일) 예보를 한꺼번에 요청
        final futures = List.generate(3, (i) {
          final d = now.add(Duration(days: i));
          final y = d.year, m = d.month.toString().padLeft(2, '0'), dd = d.day.toString().padLeft(2, '0');
          return fetchDailyForecast(searchDate: "$y-$m-$dd");
        });

        final results = (await Future.wait(futures)).expand((e) => e).toList();
        final filtered = results
            .where((e) => e['informCode'] == 'PM10')
            .toList()
          ..sort((a, b) => (a['informData'] as String).compareTo(b['informData']));

        setState(() => _forecastList = filtered);

    // 🔴 선택된 시/도 등급 확인용 로그
    if (_forecastList.isNotEmpty) {
      final test = extractZoneGrade(_forecastList.first['informGrade'] ?? '', widget.zone);
      print('✅ ${_forecastList.first['informData']} ${widget.zone} 등급 = $test');
    }
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;

    if (_forecastList.isEmpty) {
      return const Scaffold(
        backgroundColor: Color(0xFFF8CCD5),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    final zoneToUse = _savedZone ?? widget.zone;

    final first = _forecastList.first;
    final imageUrls = [
      first['imageUrl1'],
      first['imageUrl2'],
      first['imageUrl3'],
      first['imageUrl4'],
      first['imageUrl5'],
      first['imageUrl6'],
    ].whereType<String>().where((url) => url.isNotEmpty).toList();


    return Scaffold(
      body: Container(
        color: const Color(0xFFF8CCD5),
        child: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.only(bottom: 32.h),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(height: 40.h),
                Center(
                  child: Text(
                    '미세먼지 예보 초미세먼지 예보',
                    style: TextStyle(
                      fontSize: screenWidth <= 360 ? 17.sp : 20.sp,
                    ),
                  ),
                ),
                SizedBox(height: 25.h),

                // 🔥 API 이미지 슬라이더
                ImageSlider(imageUrls: imageUrls),

                SizedBox(height: 20.h),

                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 16.w),
                  child: Text(
                    '예보는 시/도 단위로 제공됩니다.',
                    style: TextStyle(
                      fontSize: screenWidth <= 360 ? 17.sp : 20.sp,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                SizedBox(height: 10.h),

                // 🔥 각 날짜별 예보 카드 (zone 기준 추출)
                ..._forecastList.map((item) {
                  final zoneGrade = extractZoneGrade(
                    item['informGrade'] ?? '',
                    zoneToUse
                    // widget.zone,
                  );

                  return ForecastCard(
                    // zone: widget.zone,
                    zone: zoneToUse,
                    presentationDate: item['dataTime'] ?? '',
                    forecastDate: item['informData'] ?? '',
                    // forecastContent: "${widget.zone}: $zoneGrade",
                    forecastContent: "$zoneToUse: $zoneGrade",
                  );
                }).toList(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
