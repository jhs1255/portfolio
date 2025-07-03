import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/utils/if_status.dart';
import 'package:intl/intl.dart';
import 'package:pi_project/utils/item.dart';
import 'package:pi_project/widgets/status_body.dart';
import 'package:pi_project/services/api_service.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:pi_project/utils/global_config.dart';
import 'package:connectivity_plus/connectivity_plus.dart';

class StatusPage extends StatefulWidget {
  final String zone;
  final String city;
  final VoidCallback? onAreaSelect;

  const StatusPage({
    super.key,
    required this.zone,
    required this.city,
    this.onAreaSelect,
  });

  @override
  State<StatusPage> createState() => _StatusPageState();
}

class _StatusPageState extends State<StatusPage> {
  String? homeName;
  int _outsidePm10 = -1;
  int _outsidePm25 = -1;
  String _currentZone = '충북';
  String _currentCity = '용암동';




  @override
  void initState() {
    super.initState();
    _loadHomeName();
    fetchOutsideDust(zone: widget.zone, city: widget.city);
    loadRecentArea();
    _fetchIfConnected();
  }

  @override
  void didUpdateWidget(covariant StatusPage oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.zone != widget.zone || oldWidget.city != widget.city) {
      fetchOutsideDust(zone: widget.zone, city: widget.city); // ✅ 수정 완료

    }
  }


  Future<void> saveRecentArea(String zone, String city) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('recentZone', zone);
    await prefs.setString('recentCity', city);
  }


  Future<void> _fetchIfConnected() async {
    final connectivity = await Connectivity().checkConnectivity();

    if (connectivity != ConnectivityResult.none) {
      await fetchOutsideDust(zone: '충북', city: '용암동'); // ← 여기서도 저장됨
    } else {
      print('❌ 인터넷 연결 안됨. 기본값 사용');
      setState(() {
        _outsidePm10 = -1;
        _outsidePm25 = -1;
      });
    }
  }

  Future<void> fetchOutsideDust({required String zone, required String city}) async {
    try {
      final data = await fetchRealtimeDustData(
        sidoName: zone,
        stationName: city,
      );

      if (data != null) {
        setState(() {
          _outsidePm10 = int.tryParse(data['pm10Value'] ?? '-1') ?? -1;
          _outsidePm25 = int.tryParse(data['pm25Value'] ?? '-1') ?? -1;
          _currentZone = zone;
          _currentCity = city;
        });
        await saveRecentArea(zone, city);               // 🔴 저장! (성공 시)
      } else {
        setState(() {
          _outsidePm10 = -1;
          _outsidePm25 = -1;
          _currentZone = zone;
          _currentCity = city;
        });
        await saveRecentArea(zone, city);               // 🔴 저장! (응답이 null이어도 지역만 기록)
      }
    } catch (e) {
      print('❌ API 호출 실패: $e');
      setState(() {
        _outsidePm10 = -1;
        _outsidePm25 = -1;

        // ✅ 실패했더라도 이 지역 이름은 보여줘야 함
        _currentZone = zone;
        _currentCity = city;
      });
    }
  }


  Future<void> loadRecentArea() async {
    final prefs = await SharedPreferences.getInstance();
    final savedZone = prefs.getString('recentZone');
    final savedCity = prefs.getString('recentCity');

    if (savedZone != null && savedCity != null) {
      _currentZone = savedZone;
      _currentCity = savedCity;
      await fetchOutsideDust(zone: savedZone, city: savedCity);
    } else {
      _currentZone = widget.zone;
      _currentCity = widget.city;
      await fetchOutsideDust(zone: widget.zone, city: widget.city);
    }
  }


  Future<void> _loadHomeName() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() => homeName = prefs.getString('homeName') ?? '미설정');
  }

  Future<void> _updateHomeName(String newName) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('homeName', newName);
    setState(() => homeName = newName);
  }

  @override
  Widget build(BuildContext context) {
    if (homeName == null) {
      return const Scaffold(
        backgroundColor: Color(0xFFF8ccd5),
        body: Center(child: CircularProgressIndicator()),
      );
    }

    return Scaffold(
      backgroundColor: const Color(0xFFF8ccd5),
      body: Stack(
        children: [
          Align(
            alignment: Alignment.center,
            child: StatusBody(
              zone: widget.zone,
              city: widget.city,
              homeName: homeName!,
              onHomeNameChanged: _updateHomeName,
              outsidePm10: _outsidePm10,
              outsidePm25: _outsidePm25,
            ),
          ),
          Positioned(
            top: 30.h,
            left: 16.w,
            child: InkWell(
              onTap: widget.onAreaSelect,
              child: StatusItem(
                icon: Icons.location_on,
                label: '현재 $_currentZone $_currentCity 미세먼지는',
                value: _outsidePm10 == -1
                    ? '정보 없음'
                    : '${getPM10Status(_outsidePm10).level}${getPM10Status(_outsidePm10).emoji}',
                iconColor: Colors.red,
              ),
            ),
          ),
          Positioned(
            top: 30.h,
            right: 16.w,
            child: ValueListenableBuilder<double>(
              valueListenable: humidity,
              builder: (context, value, _) {
                return StatusItem(
                  icon: Icons.water_drop,
                  label: '습도',
                  value: '${value.toStringAsFixed(1)}%',
                  iconColor: Colors.blueAccent,
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
