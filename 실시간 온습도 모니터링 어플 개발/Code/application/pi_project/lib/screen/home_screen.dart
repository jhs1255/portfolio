import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'status_page.dart';
import 'package:pi_project/utils/global_config.dart';
import 'forecast_page.dart';
import 'dart:math';
import 'area_choose.dart'; // 🔴 추가

class HomeScreen extends StatefulWidget {
  final String zone;
  final String city;
  const HomeScreen({super.key, required this.zone, required this.city});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final PageController _pageController = PageController();
  int _selectedIndex = 0;

  // 🔴 현재 선택된 지역 상태
  String? currentZone;
  String? currentCity;


  // 23~51줄사이는 랜덤값이 바뀌는 테스트코드임.
  // @override //global에서 온습도 테스트
  // void initState() {
  //   void initState() {
  //     super.initState();
  //     currentZone = widget.zone; // 🔴 추가
  //     currentCity = widget.city; // 🔴 추가
  //   }
  //   startTestAutoUpdate(); // global_config.dart에서 가져온 함수 실행
  // }
  //
  // @override
  // void startTestAutoUpdate() {
  //   final Random random = Random();
  //
  //   Future.doWhile(() async {
  //     await Future.delayed(Duration(seconds: 5));
  //
  //     // 0~180 범위의 랜덤값 생성
  //     humidity.value = random.nextInt(101);      // 0 ~ 100%
  //     temperature.value = 15 + random.nextInt(15); // 15 ~ 29도ㅌ
  //     homePm10.value = random.nextInt(181);      // 0 ~ 180
  //     homePm25.value = random.nextInt(181);      // 0 ~ 180
  //
  //     print('🌀 자동 갱신 - 온도: ${temperature.value}도, 습도: ${humidity.value}%');
  //     print('🌫️ 미세먼지(PM10) - 실내: ${homePm10.value}, 실외: ${outsidePm10.value}');
  //     print('🌫️ 초미세먼지(PM2.5) - 실내: ${homePm25.value}, 실외: ${outsidePm25.value}');
  //
  //     return true; // 반복 계속
  //   });
  // }


  @override
  void initState() {
    super.initState();
    currentZone = widget.zone; // 🔴 추가
    currentCity = widget.city; // 🔴 추가
  }

  // 🔴 지역 선택 페이지 이동 함수
  Future<void> _selectArea() async {
    final result = await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const AreaChoosePage()),
    );

    if (result != null && mounted) {
      setState(() {
        currentZone = result['zone'];
        currentCity = result['city'];
      });
    }
  }

  // 하단 탭 이동
  void _onTap(int index) {
    setState(() {
      _selectedIndex = index;
      _pageController.jumpToPage(index); // 페이지 이동
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: PageView(
        controller: _pageController,
        onPageChanged: (index) => setState(() => _selectedIndex = index),
        children: [
          StatusPage(
            zone: currentZone ?? widget.zone, // 🔴 수정됨
            city: currentCity ?? widget.city, // 🔴 수정됨
            onAreaSelect: _selectArea,        // 🔴 콜백 전달
          ),
          ForecastPage(zone: currentZone ?? widget.zone,), // 🔴 const 안 붙임, 원래대로 유지
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _selectedIndex,
        onTap: _onTap,
        selectedItemColor: Colors.orange,
        unselectedItemColor: Colors.orange,
        items: [
          BottomNavigationBarItem(
            icon: Icon(
              Icons.cloud,
              size: 20.h,
            ),
            label: '상태',
          ),
          BottomNavigationBarItem(
            icon: Icon(
              Icons.map,
              size: 20.h,
            ),
            label: '예보',
          ),
        ],
      ),
    );
  }
}
