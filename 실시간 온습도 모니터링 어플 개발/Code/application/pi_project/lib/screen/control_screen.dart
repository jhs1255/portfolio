import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:intl/intl.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:http/http.dart' as http;
import 'package:permission_handler/permission_handler.dart';
import 'package:pi_project/utils/global_config.dart';
import 'package:pi_project/utils/permission_utils.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../config/config.dart';

class ControlScreen extends StatefulWidget {
  const ControlScreen({super.key});

  @override
  State<ControlScreen> createState() => _ControlScreenState();
}

class _ControlScreenState extends State<ControlScreen> {
  List<BluetoothDevice> connectDevice = [];
  String wifiName = "연결안됨";
  String bluetoothName = "연결안됨";
  String humidifierState = 'OFF';
  String purifierState = 'OFF';
  String sensorState = 'OFF';
  String modeState = '수동';

  // 자동 모드 리스너 등록용 플래그
  bool _autoListenersAdded = false;

  @override
  void initState() {
    super.initState();
    _loadDeviceStates();
    _initBluetooth();
    _initWifiName();
  }

  // 화면 실행 시 기본으로 보여질 부분
  Future<void> _loadDeviceStates() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      humidifierState = prefs.getString('humidifierState') ?? 'OFF';
      purifierState = prefs.getString('purifierState') ?? 'OFF';
      sensorState = prefs.getString('sensorState') ?? 'OFF';
      modeState = prefs.getString('modeState') ?? '수동';
    });
  } //_loadDeviceStates

  //블루투스 기기연결 정보
  Future<void> _initBluetooth() async{
    //권한 요청
    await requestBluetoothPermission();
    //연결된 기기 가져오기
    List<BluetoothDevice> devices = await FlutterBluePlus.connectedDevices;

    setState(() {
      connectDevice = devices;

      if(devices.isNotEmpty){
        bluetoothName = devices[0].name.isNotEmpty ? devices[0].name : "알 수 없음";
      }else{
        bluetoothName = "연결안됨";
      }

    });
  } //_initBluetooth

  //블루투스 권한 허용 Android12 이상에서 권한 필요
  Future<void> requestBluetoothPermission() async{
    if(await Permission.bluetoothConnect.isDenied || await Permission.bluetoothConnect.isPermanentlyDenied){
      await Permission.bluetoothConnect.request();
    }

    if(await Permission.location.isDenied || await Permission.location.isPermanentlyDenied){
      await Permission.location.request();
    }
  } //requestBluetoothPermission

  Future<void> _initWifiName() async{
    String? name = await PermissionUtil.getWifiName(context);
    setState(() {
      wifiName = name;
    });
  } // _initWifiName

  Future<void> _saveDeviceState(String key, String value) async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setString(key, value);
  } //_saveDeviceState

  //블루투스를 통한 센서값 가져오기
  void startListening(BluetoothCharacteristic characteristic) {
    listenToCharacteristic(characteristic);
  }// startListening

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final formattedDate = DateFormat('yyyy-MM-dd HH:mm').format(DateTime.now());

    return Scaffold(
        backgroundColor: const Color(0xFFF8ccd5),
        appBar: AppBar(
          title: Text(
            'RemoteController',
            style: TextStyle(
              color: Colors.black,
              fontSize: screenWidth <= 360 ? 17.sp : 20.sp,
            ),
          ),
          backgroundColor: const Color(0xFFF8ccd5),
          elevation: 0,
          centerTitle: true,
        ),
        body: Padding(
          padding: EdgeInsets.all(20.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('현재 상태',
                          style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: screenWidth <= 360 ? 14.sp : 16.sp)),
                      SizedBox(height: 5.h),

                      // 온도 값 변경 감지용
                      ValueListenableBuilder<double>(
                        valueListenable: temperature,
                        builder: (context, value, _) {
                          return Text(
                            '전원 : $sensorState   온도 : ${value.toStringAsFixed(1)}°C',
                            style: TextStyle(fontSize: 14.sp),
                          );
                        },
                      ),

                      // 습도 값 변경 감지용
                      ValueListenableBuilder<double>(
                        valueListenable: humidity,
                        builder: (context, value, _) {
                          return Text(
                            '모드 : $modeState   습도 : ${value.toStringAsFixed(1)}%',
                            style: TextStyle(fontSize: 14.sp),
                          );
                        },
                      ),
                    ],
                  ),
                  Container(
                    padding: EdgeInsets.all(8.w),
                    decoration: BoxDecoration(
                      border: Border.all(color: Colors.black),
                    ),
                    child: Text(
                      '현재날짜\n$formattedDate',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: screenWidth <= 360 ? 12.sp : 14.sp,
                      ),
                    ),
                  )
                ],
              ),
              SizedBox(height: 10.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('와이파이 : $wifiName'),
                  Text('블루투스 : $bluetoothName'),
                ],
              ),
              SizedBox(height: 20.h),
              Center(child: Text('공기청정기', style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp))),
              SizedBox(height: 10.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _powerControlButton('ON', 'purifier', screenWidth),
                  SizedBox(width: 30.w),
                  _powerControlButton('OFF', 'purifier', screenWidth),
                ],
              ),
              SizedBox(height: 20.h),
              Center(child: Text('가습기', style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp))),
              SizedBox(height: 10.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _powerControlButton('ON', 'humidifier', screenWidth),
                  SizedBox(width: 30.w),
                  _powerControlButton('OFF', 'humidifier', screenWidth),
                ],
              ),
              SizedBox(height: 20.h),
              Center(child: Text('모드', style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp))),
              SizedBox(height: 10.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _modeControlButton('자동', screenWidth),
                  SizedBox(width: 30.w),
                  _modeControlButton('수동', screenWidth),
                ],
              ),
              SizedBox(height: 20.h),
              Center(child: Text('센서', style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp))),
              SizedBox(height: 10.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _powerControlButton('ON', 'sensor', screenWidth),
                  SizedBox(width: 30.w),
                  _powerControlButton('OFF', 'sensor', screenWidth),
                ],
              ),
            ],
          ),
        ),
      );
  } //build

  Widget _modeControlButton(String label, double screenWidth) {
    bool isActive = (modeState == label);
    return ElevatedButton(
      onPressed: () async {
        setState(() {
          modeState = label;
          _saveDeviceState('modeState', label);
        });

        if (label == '자동') {
          // 센서가 꺼져 있다면 켜기
          if (sensorState == 'OFF') {
            await PowerCommand('sensor', 'on'); // 센서 켜기
            setState(() {
              sensorState = 'ON';
              _saveDeviceState('sensorState', 'ON');
            });

            await Future.delayed(Duration(seconds: 5));
          }

          _setupAutoModeListeners();

        }

      },
      style: ElevatedButton.styleFrom(
        fixedSize: Size(90.w, 90.h),
        backgroundColor: isActive ? Colors.blue : Colors.white,
        foregroundColor: isActive ? Colors.white : Colors.black,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10.r),
        ),
        side: const BorderSide(color: Colors.black),
        elevation: 2,
      ),
      child: Text(label, style: TextStyle(fontSize: screenWidth <= 360 ? 16.sp : 18.sp)),
    );
  } //_modeControlButton

  Widget _powerControlButton(String label, String target, double screenWidth) {
    bool isActive = false;
    if (target == 'humidifier') isActive = (humidifierState == label);
    else if (target == 'purifier') isActive = (purifierState == label);
    else if (target == 'sensor') isActive = (sensorState == label);

    return ElevatedButton(
      onPressed: () async {
        final command = label.toLowerCase();

        // 자동 모드 상태에서 사용자가 직접 버튼을 누르면 수동으로 전환
        if (modeState == '자동') {
          setState(() {
            modeState = '수동';
            _saveDeviceState('modeState', '수동');
          });

          // 수동 모드 전환 시 리스너 해제
          _removeAutoModeListeners();

          // 알림 (선택 사항)
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('수동 제어로 전환되었습니다')),
          );
        }

        setState(() {
          if (target == 'humidifier') {
            humidifierState = label;
            _saveDeviceState('humidifierState', label);
          } else if (target == 'purifier') {
            purifierState = label;
            _saveDeviceState('purifierState', label);
          } else if (target == 'sensor') {
            sensorState = label;
            _saveDeviceState('sensorState', label);
          }
        });

        await PowerCommand(target, command);
      },
      style: ElevatedButton.styleFrom(
        fixedSize: Size(90.w, 90.h),
        backgroundColor: isActive ? Colors.blue : Colors.white,
        foregroundColor: isActive ? Colors.white : Colors.black,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10.r),
        ),
        side: const BorderSide(color: Colors.black),
        elevation: 2,
      ),
      child: Text(label, style: TextStyle(fontSize: screenWidth <= 360 ? 16.sp : 18.sp)),
    );
  } //_powerControlButton

  Future<void> PowerCommand(String target, String command) async {
    // 라즈베리파이와 블루투스 송신을 위한 SSID
    final String serviceUUID = Config.serviceUUID;
    final String characteristicUUID = Config.characteristicUUID;

    // 스위치 봇을 제어하기 위한 SSID
    final String switchbotMac = "D2:C7:C6:46:61:30"; // 스위치 봇의 MAC주소
    final Guid servicesUUID = Guid("cba20d00-224d-11e6-9fb8-0002a5d5c51b"); //공식 servive UUID
    final Guid charUUID = Guid("cba20002-224d-11e6-9fb8-0002a5d5c51b"); //공식 charUUID
    final List<int> pressCommand = [0x57, 0x01, 0x00]; //버튼 동작 명령어

    // SwitchBot Bot 전용 BLE 제어 (가습기 버튼 ON일 때)
    if (target == 'purifier') {
      try {
        await FlutterBluePlus.startScan(timeout: const Duration(seconds: 5));
        BluetoothDevice? switchBot;

        // scanResults를 listen으로 받아야 실시간 검색 가능
        final subscription = FlutterBluePlus.scanResults.listen((results) {
          for (var result in results) {
            if (result.device.id.id == switchbotMac) {
              switchBot = result.device;
              FlutterBluePlus.stopScan();
              print("SwitchBot 발견됨: ${switchBot!.name}");
              break;
            }
          }
        });

        // BLE 스캔 대기
        await Future.delayed(const Duration(seconds: 5));
        await subscription.cancel();

        if (switchBot == null) {
          throw Exception("SwitchBot 기기를 찾지 못했습니다.");
        }

        await switchBot!.connect(autoConnect: false);
        print("SwitchBot 연결됨");

        final services = await switchBot!.discoverServices();
        for (BluetoothService service in services) {
          if (service.uuid == servicesUUID) {
            for (BluetoothCharacteristic char in service.characteristics) {
              if (char.uuid == charUUID && char.properties.writeWithoutResponse) {
                await char.write(pressCommand, withoutResponse: true);
                print("SwitchBot 명령 전송 완료");
                await switchBot!.disconnect();
                await FlutterBluePlus.stopScan();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('SwitchBot 작동 완료 ! 잠시 기다리세요')),
                );
              }
            }
          }
        }

        throw Exception("SwitchBot characteristic 찾기 실패");

      } catch (e) {
        print("SwitchBot 전송 실패: $e");
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('SwitchBot 명령 실패: $e')),
        );
        return;
      }
    }

    //블루투스가 연결되어 있는 경우
    if(connectDevice.isNotEmpty){
      try{
        BluetoothDevice device = connectDevice[0];
        // 장치 연결 시도
        try {
          await device.connect(autoConnect: false);
          print("기기 연결 완료");
        } catch (e) {
          if (e.toString().contains('already connected')) {
            print("이미 연결된 상태입니다.");
          } else {
            throw Exception("기기 연결 실패: $e");
          }
        }

        //GATT서버에서 광고하는 블루투스의 서비스 및 characteristic탐색
        List<BluetoothService> services = await device.discoverServices();
        for(BluetoothService service in services){
          for(BluetoothCharacteristic characteristic in service.characteristics){
            if(service.uuid.toString() == serviceUUID && characteristic.uuid.toString() == characteristicUUID && characteristic.properties.write){
              String message = "$target : $command";
              await characteristic.write(utf8.encode(message));

              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('[$target] $command 전송 성공')),
              );
              print("[$target] $command 전송 성공");

              await characteristic.setNotifyValue(true); // notify 시작

              startListening(characteristic);

              return;
            }
          }// for characteristic
        }// for service

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('[$target] $command 전송 실패 : write 가능한 characterisitic 없음')),
        );
        print("[$target] $command 전송 실패 : write 가능한 characteristic 없음");
      }catch(e){
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('[$target] $command 전송중 오류 : $e')),
        );
        print("[$target] $command 전송중 오류 발생 : $e");
      }
    }
    //블루투스는 연결되어 있지 않고 와이파이만 연결된 경우
    else{
      final url = Uri.parse("http://${Config.baseURL}/power/$target");

      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json; charset=UTF-8'},
        body: jsonEncode({'message': command}),
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('[$target] $command 성공 : ${response.body}')),
        );
        print("[$target] $command 성공 : ${response.body}");
        // WebSocket 연결은 센서 ON 명령 후에만 실행
        if (target == 'sensor' && command == 'on') {
          print("WebSocket 연결 시작됨");
          connectToSensorWebSocket(); // ← 전역 함수 global_config.dart 에 정의된 WebSocket 연결 함수
        }
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('[$target] $command 실패 : ${response.body}')),
        );
        print("[$target] $command 실패 : ${response.body}");
      } //if-esle
    }

  } //PowerCommand

  void _setupAutoModeListeners() {
    if (_autoListenersAdded) return;

    humidity.addListener(_handleAutoHumidity);
    homePm25.addListener(_handleAutoDust);
    homePm10.addListener(_handleAutoDust);
    _autoListenersAdded = true;
  } //_setAutoModeListeners

  void _removeAutoModeListeners() {
    if (!_autoListenersAdded) return;

    humidity.removeListener(_handleAutoHumidity);
    homePm25.removeListener(_handleAutoDust);
    homePm10.removeListener(_handleAutoDust);
    _autoListenersAdded = false;
  } // _removeAtuoModeListeners

  //자동 모드일때 실시간 습도 검사
  void _handleAutoHumidity() {
    if (modeState != '자동') return;

    final h = humidity.value;

    if (h < 40.0 && humidifierState != 'ON') {
      PowerCommand('humidifier', 'on');
      humidifierState = 'ON';
      _saveDeviceState('humidifierState', 'ON');
      setState(() {});
    } else if (h >= 60.0 && humidifierState != 'OFF') {
      PowerCommand('humidifier', 'off');
      humidifierState = 'OFF';
      _saveDeviceState('humidifierState', 'OFF');
      setState(() {});
    }
  } //_handleAtuoHumidity

  //자동 모드일 때 실시간 먼지 농도검사
  void _handleAutoDust() {
    if (modeState != '자동') return;

    final pm25 = homePm25.value;
    final pm10 = homePm10.value;

    if ((pm25 >= 25 || pm10 > 50) && purifierState != 'ON') {
      PowerCommand('purifier', 'on');
      purifierState = 'ON';
      _saveDeviceState('purifierState', 'ON');
      setState(() {});
    } else if ((pm25 < 25 && pm10 <= 50) && purifierState != 'OFF') {
      PowerCommand('purifier', 'off');
      purifierState = 'OFF';
      _saveDeviceState('purifierState', 'OFF');
      setState(() {});
    }
  } // _handleAutoDust

} // _ControlScreenState
