import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class StatusItem extends StatelessWidget {
  final IconData? icon;         // 아이콘 (예: Icons.water_drop)
  final String label;          // 상단 텍스트 (예: '습도')
  final String value;          // 수치 값 (예: '30%')
  final Color iconColor;       // 아이콘 색상

  const StatusItem({
    Key? key,
    this.icon,
    required this.label,
    required this.value,
    this.iconColor = Colors.black, // 기본값: 검정색
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    // 화면 크기 가져오기
    final screenWidth = MediaQuery.of(context).size.width;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          children: [
            Icon(icon, size: screenWidth <= 360 ? 15.w : 17.w, color: iconColor),
            SizedBox(width: 4.w),
            Text(
              label,
              style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 17.sp, fontWeight: FontWeight.bold),
            ),
          ],
        ),
        SizedBox(height: 4.h),
        Text(
          value,
          style: TextStyle(fontSize: screenWidth <= 360 ? 18.sp : 20.sp, color: Colors.black54, fontWeight: FontWeight.bold),
        ),
      ],
    );
  }
}
