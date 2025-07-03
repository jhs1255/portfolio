import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart'; // 추가!
import 'package:pi_project/utils/if_status.dart';

class ForecastCard extends StatelessWidget {
  final String zone;
  final String presentationDate;
  final String forecastDate;
  final String forecastContent;

  const ForecastCard({
    Key? key,
    required this.zone,
    required this.presentationDate,
    required this.forecastDate,
    required this.forecastContent,
  }) : super(key: key);

  String extractLevelFromContent() {
    final entries = forecastContent.split(',');
    final reg = RegExp('^\\s*$zone\\s*:', caseSensitive: false);
    final match = entries.firstWhere(
      (e) => reg.hasMatch(e.trim()),
      orElse: () => '$zone: 정보 없음',
    );
    return match.replaceFirst(reg, '').trim();
  }

  @override
  Widget build(BuildContext context) {
    final String level = extractLevelFromContent();
    final DustStatus status = getStatusFromLevel(level);

    return Container(
      margin: EdgeInsets.all(8.w),
      padding: EdgeInsets.all(10.w),
      decoration: BoxDecoration(
        border: Border.all(color: status.color, width: 2.w),
        color: Colors.white,
        borderRadius: BorderRadius.circular(10.r),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            "📢 발표일시: $presentationDate",
            style: TextStyle(
              fontSize: ScreenUtil().screenWidth <= 360 ? 17.sp : 19.sp,
            ),
          ),
          SizedBox(height: 8.h),
          Text(
            "🏡 측정소: $zone",
            style: TextStyle(
              fontSize: ScreenUtil().screenWidth <= 360 ? 17.sp : 19.sp,
            ),
          ),
          SizedBox(height: 8.h),
          Text(
            "🕒 예보일시: $forecastDate",
            style: TextStyle(
              fontSize: ScreenUtil().screenWidth <= 360 ? 17.sp : 19.sp,
            ),
          ),
          SizedBox(height: 8.h),
          Row(
            children: [
              Text(
                "📊 예보: ${status.level}",
                style: TextStyle(
                  fontSize: ScreenUtil().screenWidth <= 360 ? 18.sp : 20.sp,
                  fontWeight: FontWeight.bold,
                  color: status.color,
                ),
              ),
              SizedBox(width: 6.w),
              Text(
                status.emoji,
                style: TextStyle(
                  fontSize: ScreenUtil().screenWidth <= 360 ? 18.sp : 20.sp,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
