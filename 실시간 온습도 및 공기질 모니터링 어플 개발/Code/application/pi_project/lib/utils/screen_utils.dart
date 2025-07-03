import 'package:flutter/material.dart';

// 갤럭시 S21+ 기준 해상도
// const double baseHeight = 2400.0;
// const double baseWidth = 1080.0;
const double baseWidth = 411.0;
const double baseHeight = 915.0;

//비율 기반 높이 계산
double proHeight(BuildContext context, double baseValue){
  final screenHeight = MediaQuery.of(context).size.height;
  return screenHeight * (baseValue / baseHeight);
}

//비율 기반 너비 계산
double proWidth(BuildContext context, double baseValue){
  final screenWidth = MediaQuery.of(context).size.width;
  return screenWidth * (baseValue / baseWidth);
}