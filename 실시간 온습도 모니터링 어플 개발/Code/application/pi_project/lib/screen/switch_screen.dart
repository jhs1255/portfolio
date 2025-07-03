import 'package:flutter/material.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/screen/control_screen.dart';
import 'package:pi_project/screen/home_screen.dart';

import 'bluetooth_connect_page.dart';

class SwitchScreen extends StatelessWidget{
  const SwitchScreen({super.key});

  @override
  Widget build(BuildContext context) {
    FlutterNativeSplash.remove();

    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;

    double iconSize = screenWidth * 0.25; // 아이콘 크기 13.89% 비율
    double textSize = screenWidth * 0.055; // 텍스트 크기 5.5% 비율
    double lineSize = screenHeight * 0.25;

    return Scaffold(
      backgroundColor: Color(0xFFF8ccd5),
      appBar: AppBar(
        title: Text('메뉴를 선택하시오',style: TextStyle(color: Colors.blueAccent, fontWeight: FontWeight.bold),),
        backgroundColor: Color(0xFFF8ccd5),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Expanded(
                  child: TextButton(
                      onPressed: (){
                        Navigator.of(context).push(
                          MaterialPageRoute(builder: (context) => const HomeScreen(zone: '청주시', city: '용암동',)),
                        );
                      },
                      child: Column(
                        children: [
                          Icon(
                            Icons.wb_sunny,
                            size: iconSize,
                            color: Colors.yellowAccent,
                          ),
                          Text('Weather', style: TextStyle(
                            fontSize: textSize,
                            color: Colors.yellowAccent
                          ),),
                        ],
                      ),
                  ),
                ),

                Container(
                  width: 2.w, // 선의 두께
                  height: lineSize, // 버튼 크기만큼 선의 높이 (아이콘 + 텍스트 높이)
                  color: Colors.white, // 선 색상
                ),

                Expanded(
                  child: TextButton(
                    onPressed: (){
                      Navigator.of(context).push(
                        MaterialPageRoute(builder: (context) => const BluetoothScreen()),
                      );
                    },
                    child: Column(
                      children: [
                        Icon(
                          Icons.settings_remote,
                          size: iconSize,
                          color: Colors.blueAccent,
                        ),
                        Text('Controller', style: TextStyle(
                          fontSize: textSize,
                          color: Colors.blueAccent
                        ),),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}