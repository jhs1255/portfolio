import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/utils/permission_utils.dart';

import 'control_screen.dart';

class BluetoothScreen extends StatefulWidget{
  const BluetoothScreen({super.key});
  @override
  State<StatefulWidget> createState() => _BluetoothScreenState();
}

class _BluetoothScreenState extends State<BluetoothScreen>{
  FlutterBluePlus flutterBlue = FlutterBluePlus(); // 라이브러리 선언

  BluetoothAdapterState _bluetoothState = BluetoothAdapterState.unknown; // 블루투스의 상태

  List<BluetoothDevice> connectedDevices = []; //연결된 기기 저장할 변수

  bool isScanning = false; //처음 스캔 기능 상태

  List<ScanResult> scanDevices = []; // 스캔 결과를 담을 변수

  // late StreamSubscription<BluetoothAdapterState> _btStateSubscription;
  // late StreamSubscription<List<ScanResult>> _scanSubscription;

  StreamSubscription<BluetoothAdapterState>? _btStateSubscription;
  StreamSubscription<List<ScanResult>>? _scanSubscription;


  @override
  void initState(){
    super.initState();
    FlutterNativeSplash.remove();
    // _checkBluetoothState();
    PermissionUtil.requestPermissions().then((_) => _checkBluetoothState());
  }

  // 블루투스 상태를 확인하는 함수
  void _checkBluetoothState() {
    _btStateSubscription = FlutterBluePlus.state.listen((state) async {
      if (!mounted) return;
      setState(() {
        _bluetoothState = state;
      });

      if(state == BluetoothAdapterState.on){

        //hsk91 라즈베리파이 연결 확인
        List<BluetoothDevice> connected = await FlutterBluePlus.connectedDevices;
        for (BluetoothDevice device in connected) {
          if (device.name == "hsk91") {
            final connState = await device.connectionState.first;
            if (connState == BluetoothConnectionState.connected) {
              if (!mounted) return;
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('${device.name} 기기가 연결되어 컨트롤 화면으로 이동합니다.')),
              );
              await Future.delayed(Duration(seconds: 2));
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(builder: (context) => ControlScreen()),
              );
              return;
            }
          }
        }
        // 스캔 시작
        _startScan();
      }
    });
  }

  //블루투스가 꺼져있다면 켜기
  Future<void> _toogleBluetooth() async{
    if(_bluetoothState == BluetoothAdapterState.off){
      try{
        await FlutterBluePlus.turnOn();
        _startScan();
      }catch(e){
        print('블루투스 켜는 중 오류 : $e');
      }
    }
  }

  //블루투스 스캔
  Future<void> _startScan() async{
    if(isScanning) return;

    //GPS 사용 여부 및 권한 부여
    bool granted = await PermissionUtil.checkLocationService(context);
    if(!granted) return;

    //스캔 가능 상태로 변경
    setState(() {
      scanDevices.clear();
      isScanning = true;
    });

    try{
      //4초간 블루투스 스캔 시작
      await FlutterBluePlus.startScan(timeout: const Duration(seconds: 4));
    }catch(e){
      print("스캔 중 예외: $e");
      if (mounted) {
        setState(() {
          isScanning = false;
        });
      }
      return;
    }

    //스캔 결과 가져오기
    _scanSubscription = FlutterBluePlus.scanResults.listen((results) {
      if (!mounted) return;
      setState(() {
        for (var result in results) {
          if (!scanDevices.any((r) => r.device.id == result.device.id)) {
            scanDevices.add(result);
          }
        }
      });
    });

    //스캔 완료 후 스캔 불가 상태로 변경
    Future.delayed(const Duration(seconds: 4),(){
      if (!mounted) return;
      setState(() {
        isScanning = false;
      });
    });

  }

  Future<void> _restartScan() async {
    // 스캔이 진행 중이면 종료
    if (isScanning) {
      await FlutterBluePlus.stopScan(); // 기존 스캔 중지
      setState(() {
        isScanning = false; // 스캔 상태 초기화
      });
    }

    // 새 스캔 시작
    setState(() {
      scanDevices.clear(); // 기존 스캔 결과 초기화
      isScanning = true; // 스캔 시작 상태
    });

    try {
      // 4초 동안 블루투스 스캔
      await FlutterBluePlus.startScan(timeout: const Duration(seconds: 4));
    } catch (e) {
      print("스캔 중 예외: $e");
      if (mounted) {
        setState(() {
          isScanning = false; // 스캔 실패 시 상태 초기화
        });
      }
      return;
    }

    // 스캔 결과를 구독하여 업데이트
    _scanSubscription = FlutterBluePlus.scanResults.listen((results) {
      if (!mounted) return;
      setState(() {
        for (var result in results) {
          if (!scanDevices.any((r) => r.device.id == result.device.id)) {
            scanDevices.add(result);
          }
        }
      });
    });
  }


  @override
  void dispose() {
    // _btStateSubscription.cancel();
    // _scanSubscription.cancel();
    _btStateSubscription?.cancel();
    _scanSubscription?.cancel();
    // 연결된 모든 BLE 기기 연결 해제
    _disconnectAllDevices();
    super.dispose();
  }

  // 연결 해제 함수 추가
  Future<void> _disconnectAllDevices() async {
    for (BluetoothDevice device in scanDevices.map((r) => r.device)) {
      final connState = await device.connectionState.first;
      if (connState == BluetoothConnectionState.connected ||
          connState == BluetoothConnectionState.connecting) {
        try {
          await device.disconnect();
        } catch (e) {
          print("연결 해제 중 오류: $e");
        }
      }
    }
  }

  @override
  Widget build(BuildContext context) {

    final screenWidth = MediaQuery.of(context).size.width;

    // 알 수 없는 기기 제외한 리스트
    final filteredDevices = scanDevices.where((result) {
      final name = result.device.name.isNotEmpty
          ? result.device.name
          : result.advertisementData.localName;
      return name.isNotEmpty && name != "알 수 없는 기기";
    }).toList();

    return Scaffold(
      //appbar를 제외한 배경화면 색 지정
      backgroundColor: Color(0xFFF8ccd5),
      appBar: AppBar(
        title: Text('BlueTooth Connetion'),
        //appbar의 배경색 지정
        backgroundColor: Color(0xFFF8ccd5),
      ),
      body: Center(
        child:_bluetoothState == BluetoothAdapterState.on ? Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(
              width: screenWidth <=360 ?  320 : 340.w,
              height: screenWidth <=360 ?  550 : 570.h,
              color: Colors.white,
              child: filteredDevices.isEmpty ? Center(child: Text('연결 가능한 기기가 없습니다.'),)
              :ListView.builder(
                itemCount: filteredDevices.length,
                itemBuilder: (context, index){
                  // final result = scanDevices[index];
                  final result = filteredDevices[index];
                  final device = result.device;
                  return ListTile(
                    title: Text(device.name.isNotEmpty ? device.name : result.advertisementData.localName,
                    ),
                    // title: Text(device.name.isNotEmpty ? device.name : result.advertisementData.localName.isNotEmpty ? result.advertisementData.localName : "알 수 없는 기기"),
                    subtitle: Text(device.id.id),
                    trailing: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blueAccent,
                        ),
                        onPressed: device.name == "hsk91" ? () async {
                          //블루투스 연결에 성공할 경우
                          try{

                            //연결 시도
                            await device.connect(
                              timeout: const Duration(seconds: 10), //10초 연결시도
                              autoConnect: false,
                            );

                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('${device.name} 연결 성공')),
                            );

                            if(!mounted) return; // 만약에 연결이 끊기면 넘어가지 않음
                            Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(builder: (context) => ControlScreen()),
                            );

                          }
                          //연결에 실패할 경우
                          catch(e){
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('연결 실패 : $e')),
                            );
                          }
                        } : null,
                        child: Text(
                          '연결하기',
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        ),
                    ),
                  );
                },
              ),
            ),

            //블루투스 버튼과 건너뛰기 사이의 공간
            SizedBox(height: 50.h,),

            Expanded(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [

                  ElevatedButton(
                    onPressed: (){
                      _restartScan();
                    },
                    child: Text('다시 스캔'),
                  ),

                  SizedBox(width: 20.w,),

                  ElevatedButton(
                    onPressed: (){
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => ControlScreen()),
                      );
                    },
                    child: Text('건너뛰기'),
                  ),
                ],
              ),
            ),

          ],
        )
            ://만약 블루투스가 꺼져 있다면 아래 값을 대입
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: screenWidth <=360 ?  180 : 200.w,
              height: screenWidth <=360 ?  180 : 200.h,
              // color: Colors.white,
              child: DecoratedBox(

                decoration: BoxDecoration(
                  color: Colors.blueAccent,
                  borderRadius: BorderRadius.circular(screenWidth <=360 ?  23.r : 25.r),
                ),

                //블루투스 켜기 버튼
                child: TextButton(
                  onPressed: _toogleBluetooth,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.bluetooth,
                        size: screenWidth <=360 ?  65.w : 70.w,
                        color: Colors.white,
                      ),
                      Text(
                        '블루투스 켜기',
                        style: TextStyle(fontSize: screenWidth <=360 ?  23.sp : 25.sp, color: Colors.white),
                      )
                    ],
                  ),
                ),

              ),

            ),

            //블루투스 버튼과 건너뛰기 사이의 공간
            SizedBox(height: 20.h,),

            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blueAccent,
              ),
              onPressed: (){
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (context) => ControlScreen()),
                );
              },
              child: Text(
                '건너뛰기',
                style: TextStyle(
                  fontSize: screenWidth <=360 ? 15.sp : 17.sp ,
                  color: Colors.white,
                ),
              ),
            ),

          ],
        ),

      ),
    );

  }
}