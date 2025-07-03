import 'dart:io';

import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:flutter/material.dart';
import 'package:pi_project/screen/switch_screen.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

void main() {
  WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
  FlutterNativeSplash.preserve(widgetsBinding: widgetsBinding);
  runApp(
    ScreenUtilInit(
      designSize: const Size(411, 915),
      minTextAdapt: true,
      splitScreenMode: true,
      builder: (context, child){
        final size = MediaQuery.of(context).size;
        final screenWidth = size.width;
        final screenHeight = size.height;
        print("Screen Width: $screenWidth, Screen Height: $screenHeight");
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          home: const BackpressedExit(),
        );
      }
    ),
  );
}

class BackpressedExit extends StatefulWidget{
  const BackpressedExit({super.key});

  @override
  State<StatefulWidget> createState() => _BackPressedExitState();
}

class _BackPressedExitState extends State<BackpressedExit>{
  DateTime? backPressedTime;

  @override
  Widget build(BuildContext context){
    return PopScope(
      canPop: false,
      onPopInvoked: (bool didPop){
        if(didPop) return;

        DateTime nowTime = DateTime.now();
        if(backPressedTime == null || nowTime.difference(backPressedTime!) > const Duration(seconds: 2)){
          backPressedTime = nowTime;
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('종료하려면 한 번 더 누르시오')),
          );
        }else{
          exit(0);
        }
      },
      child: SwitchScreen(),
    );
  }

}