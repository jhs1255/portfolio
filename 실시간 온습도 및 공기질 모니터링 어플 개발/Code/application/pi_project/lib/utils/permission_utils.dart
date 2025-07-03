import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:network_info_plus/network_info_plus.dart';
import 'package:permission_handler/permission_handler.dart';

class PermissionUtil{

  static Future<bool> checkLocationService(BuildContext context) async{
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if(!serviceEnabled){
      bool? result = await _showDialog(context);
      if(result == true){
        Navigator.of(context, rootNavigator: true).pop();
        await Geolocator.openLocationSettings();
        await Future.delayed(const Duration(seconds: 2));
        return await Geolocator.isLocationServiceEnabled();
      }
      return false;
    }
    return true;

  }

  //위치서비스 켜기
  static Future<bool?> _showDialog(BuildContext context){
    return showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('위치 서비스 필요'),
        content: const Text('블루투스 검색을 하려면 위치 서비스를 사용해야 합니다. 설정으로 이동 하시겠습니까?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context, false), child: const Text('취소')),
          TextButton(onPressed: () => Navigator.pop(context, true), child: const Text('설정')),
        ],
      ),
    );
  }

  //블루투스 권한 부여
  static Future<void> requestPermissions() async {
    await [
      Permission.bluetooth,
      Permission.bluetoothScan,
      Permission.bluetoothConnect,
      Permission.bluetoothAdvertise,
      Permission.locationWhenInUse,
      Permission.location,
    ].request();
  }

  //WIFI-SSID 와이파이 이름 가져오기
  static Future<String> getWifiName(BuildContext context) async{
    //위치 서비스 켜져 있는지 확인
    bool isLocationEnable = await checkLocationService(context);
    if(!isLocationEnable){ return '위치 서비스 꺼짐';}

    //위치 권한 요청
    PermissionStatus status = await Permission.location.request();
    if(!status.isGranted){ return '위치 권한 거부됨';}

    final info = NetworkInfo();
    String? wifiName = await info.getWifiName();
    String? wifiSSID = await info.getWifiBSSID();

    // SSID 문자열 처리 (iOS에서는 ""로 감싸져 있는 경우 있음)
    if (wifiName != null && wifiName.startsWith('"') && wifiName.endsWith('"')) {
      wifiName = wifiName.substring(1, wifiName.length - 1);
    }

    //와이파이가 연결 안 되어 있을 경우
    if(wifiSSID == null || wifiSSID.toLowerCase() == "00:00:00:00:00:00"){ return'연결안됨';}

    //와이파이가 연결이 되어 있지만 이름을 불러올 수 없는 경우
    if(wifiName == null || wifiName.isEmpty){ return '알 수 없음';}

    return wifiName;

  }

}