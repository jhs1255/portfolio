import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/utils/global_config.dart';
import 'package:pi_project/screen/status_page.dart';
import 'package:pi_project/services/api_service.dart'; // 🔴 API 서비스 import
import 'home_screen.dart';

class AreaChoosePage extends StatefulWidget {
  const AreaChoosePage({Key? key}) : super(key: key);

  @override
  State<AreaChoosePage> createState() => _AreaChoosePageState();
}

class _AreaChoosePageState extends State<AreaChoosePage> {

  List<Map<String, dynamic>> favorites = [];
  List<Map<String, dynamic>> searchedStations = [];
  String searchQuery = '';
  bool isSearching = false;

  @override
  void initState() {
    super.initState();
    loadFavorites();

    // 🔴 초기 진입 시 기본값으로 API 호출
    fetchStationList('청주').then((list) {
      setState(() {
        searchedStations = list;
      });
    });
  }

  Future<void> saveFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final List<String> encodedList = favorites.map((item) => jsonEncode(item)).toList();
    await prefs.setStringList('favorites', encodedList);
  }

  Future<void> loadFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final List<String>? stored = prefs.getStringList('favorites');
    if (stored != null) {
      setState(() {
        favorites = stored.map((str) => Map<String, dynamic>.from(jsonDecode(str))).toList();
      });
    }
  }

  void showLimitAlert() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text("알림", style: TextStyle(fontSize: 18.sp)),
        content: Text("6개 이상 즐겨찾기를 할 수 없어요.", style: TextStyle(fontSize: 16.sp)),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: Text("확인", style: TextStyle(fontSize: 14.sp)),
          )
        ],
      ),
    );
  }

  void goToStatusPage(String selectedZone, String selectedCity) async {
    // 🔴 API 호출 추가
    final dust = await fetchRealtimeDustData(
      sidoName: selectedZone,
      stationName: selectedCity,
    );

    if (dust != null) {
      print('✅ $selectedCity PM10: ${dust['pm10Value']}, PM2.5: ${dust['pm25Value']}');
    } else {
      print('❗ 해당 stationName($selectedCity) 없음');
    }

    Navigator.pop(context, {
      'zone': selectedZone,
      'city': selectedCity,
    });
  }


  @override
  Widget build(BuildContext context) {
    final DateTime now = DateTime.now();
    final String formatted =
    now.toString().substring(0, 16); // “yyyy-MM-dd HH:mm” 형태
    final List<Map<String, dynamic>> dataSource = searchedStations.isNotEmpty
        ? searchedStations
        : (searchQuery.isEmpty
        ? allRegionData.map((e) => Map<String, dynamic>.from(e)).toList()
        : allRegionData
        .where((item) => item['sidoName']!.contains(searchQuery) || item['stationName']!.contains(searchQuery))
        .map((e) => Map<String, dynamic>.from(e))
        .toList());

    return Scaffold(
      appBar: AppBar(
        title: isSearching
            ? TextField(
          autofocus: true,
          decoration: InputDecoration(
            hintText: '지역명 검색',
            border: InputBorder.none,
            hintStyle: TextStyle(fontSize: 14.sp),
          ),
          onChanged: (value) async {
            setState(() => searchQuery = value);
            if (value.isEmpty) {
              setState(() => searchedStations = []);
            } else {
              final List<Map<String, dynamic>> apiList = await fetchStationList(value);
              setState(() => searchedStations = apiList);
            }
          },
        )
            : Text('지역 선택', style: TextStyle(fontSize: 18.sp)),
        actions: [
          IconButton(
            icon: Icon(isSearching ? Icons.close : Icons.search, size: 22.w),
            onPressed: () {
              setState(() {
                isSearching = !isSearching;
                if (!isSearching) searchQuery = '';
                searchedStations = [];
              });
            },
          )
        ],
      ),
      backgroundColor: const Color(0xFFF2F6FF),
      body: Padding(
        padding: EdgeInsets.all(16.w),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('측정 시간: $formatted', style: TextStyle(fontSize: 16.sp, fontWeight: FontWeight.bold)),
            SizedBox(height: 10.h),
            Wrap(
              spacing: 8.w,
              runSpacing: 6.h,
              children: favorites.map((favItem) {
                return ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.orangeAccent,
                    padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10.r),
                    ),
                  ),
                  onPressed: () {
                    goToStatusPage(
                      favItem['sidoName']!,
                      favItem['stationName']!,
                    );
                  },
                  onLongPress: () {
                    setState(() {
                      favorites.remove(favItem);
                      saveFavorites();
                    });
                  },
                  child: Text('${favItem['sidoName']} ${favItem['stationName']}', style: TextStyle(fontSize: 14.sp)),
                );
              }).toList(),
            ),
            SizedBox(height: 10.h),
            Expanded(
              child: ListView.builder(
                itemCount: dataSource.length,
                itemBuilder: (context, index) {
                  final item = dataSource[index];
                  return Card(
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.r)),
                    margin: EdgeInsets.symmetric(vertical: 6.h),
                    child: ListTile(
                      contentPadding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 10.h),
                      title: Text('시도명 / ${item['sidoName']}', style: TextStyle(fontSize: 15.sp)),
                      subtitle: Text('측정소명 / ${item['stationName']}', style: TextStyle(fontSize: 13.sp)),
                      trailing: IconButton(
                        icon: Icon(Icons.add, size: 20.w),
                        onPressed: () async {
                          final sido = item['sidoName']!;
                          final station = item['stationName']!;

                          // 🔴 미세먼지 데이터 받아오기 (추가된 부분)
                          final dust = await fetchRealtimeDustData(
                            sidoName: sido,
                            stationName: station,
                          );
                          if (dust != null) {
                            print('✅ $station PM10: ${dust['pm10Value']}, PM2.5: ${dust['pm25Value']}');
                          } else {
                            print('❌ $station 측정소의 미세먼지 정보 없음');
                          }

                          if (favorites.length >= 6) {
                            showLimitAlert();
                          } else {
                            if (!favorites.any((fav) => fav['sidoName'] == sido && fav['stationName'] == station)) {
                              setState(() {
                                favorites.add({
                                  'sidoName': sido,
                                  'stationName': station,
                                });
                                saveFavorites();
                              });
                            }
                          }
                        },
                      ),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
