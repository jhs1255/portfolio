import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/utils/if_status.dart';

class DustCircle extends StatelessWidget {
  final String label;
  final DustStatus outer;
  final DustStatus inner;

  const DustCircle({
    Key? key,
    required this.label,
    required this.outer,
    required this.inner,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    return Center(
      child: Stack(
        alignment: Alignment.center,
        children: [
          Container(
            width: 235.w,
            height: 235.h,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: outer.color.withOpacity(1),
            ),
          ),
          Container(
            width: 155.w,
            height: 155.h,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              color: inner.color.withOpacity(1),
            ),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(label, style: TextStyle(fontSize: screenWidth <= 360 ? 20.sp : 23.sp, fontWeight: FontWeight.bold,color: Colors.white)),
                  SizedBox(height: 6.h),
                  Text(inner.level, style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp,color: Colors.white)),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
