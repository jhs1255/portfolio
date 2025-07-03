// ✅ 미세먼지 상태 본체 UI - StatusBody
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/widgets/dust_circle.dart';
import 'package:pi_project/widgets/dust_compare.dart';
import 'package:pi_project/utils/global_config.dart';
import 'package:pi_project/utils/if_status.dart';

class StatusBody extends StatefulWidget {
  final String zone;
  final String city;
  final String homeName;
  final Function(String) onHomeNameChanged;

  // 🔴 새로 추가한 필드
  final int outsidePm10;
  final int outsidePm25;

  const StatusBody({
    Key? key,
    required this.zone,
    required this.city,
    required this.homeName,
    required this.onHomeNameChanged,
    required this.outsidePm10,  // ✅ 올바른 형태
    required this.outsidePm25,  // ✅ 올바른 형태
  }) : super(key: key);

  @override
  State<StatusBody> createState() => _StatusBodyState();
}

class _StatusBodyState extends State<StatusBody> {
  final PageController _pageController = PageController(); // DustCircle용
  final PageController _pageController2 = PageController(); // DustCompareCard용
  int _currentPage = 0; // 인디케이터 관리용

  Widget _buildDot(int index) {
    return Container(
      width: 8.w,
      height: 8.h,
      margin: EdgeInsets.symmetric(horizontal: 2.w),
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: _currentPage == index ? Colors.blue : Colors.grey[300],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final now = DateTime.now();
    final formatted = now.toString().substring(0, 16);

    return SingleChildScrollView(
      padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom, top: 50.h),
      child: Column(
        children: [
          // ✅ 상단: 감지 시각
          Text('미세먼지 감측 시간',
              style: TextStyle(fontSize: screenWidth <= 360 ? 23.sp : 27.sp, fontWeight: FontWeight.bold)),
          Text('$formatted', style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 18.sp)),
          SizedBox(height: 4.0.h),

          // ✅ 원형 시각화 PageView
          Container(
            width: 250.w,
            height: 250.h,
            child: Stack(
              children: [
                PageView(
                  controller: _pageController,
                  onPageChanged: (index) {
                    setState(() => _currentPage = index);
                    _pageController2.jumpToPage(index);
                  },
                  children: [
                    ValueListenableBuilder(
                      valueListenable: ValueNotifier(widget.outsidePm25), // ✅ 고침 // ✅ int → ValueNotifier로 감싸줌
                      builder: (context, outVal, _) {
                        return ValueListenableBuilder(
                          valueListenable: homePm25,
                          builder: (context, homeVal, _) {
                            return DustCircle(
                              label: '미세먼지',
                              outer: getPM10Status(outVal),
                              inner: getPM10Status(homeVal),
                            );
                          },
                        );
                      },
                    ),
                    ValueListenableBuilder(
                      valueListenable: ValueNotifier(widget.outsidePm10), // ✅ 고침 // ✅ 이것도 마찬가지
                      builder: (context, outVal, _) {
                        return ValueListenableBuilder(
                          valueListenable: homePm10,
                          builder: (context, homeVal, _) {
                            return DustCircle(
                              label: '초미세먼지',
                              outer: getPM25Status(outVal),
                              inner: getPM25Status(homeVal),
                            );
                          },
                        );
                      },
                    ),
                  ],
                ),
                Positioned(
                  bottom: 8,
                  right: 8,
                  child: Row(
                    children: [
                      _buildDot(0),
                      SizedBox(width: 6.w),
                      _buildDot(1),
                    ],
                  ),
                ),
              ],
            ),
          ),

          // ✅ 비교 카드 PageView
          SizedBox(height: 10.0.h),
          Container(
            height: 390.h,
            width: 340.w,
            child: Stack(
              children: [
                PageView(
                  controller: _pageController2,
                  onPageChanged: (index) {
                    setState(() => _currentPage = index);
                    _pageController.jumpToPage(index);
                  },
                  children: [
                    ValueListenableBuilder(
                      valueListenable: homePm25,
                      builder: (context, homeVal, _) {
                        return ValueListenableBuilder(
                          valueListenable: ValueNotifier(widget.outsidePm25), // ✅ 고침
                          builder: (context, outVal, _) {
                            return DustCompareCard(
                              homeName: widget.homeName!,
                              homeStatus: getPM10Status(homeVal),
                              outsideStatus: getPM10Status(outVal),
                              homeLabel: "${widget.homeName} 미세먼지",
                              outLabel: "${widget.zone} 미세먼지",
                              name: "미세먼지",
                              onHomeNameChanged: widget.onHomeNameChanged,
                            );
                          },
                        );
                      },
                    ),
                    ValueListenableBuilder(
                      valueListenable: homePm10,
                      builder: (context, homeVal, _) {
                        return ValueListenableBuilder(
                          valueListenable: ValueNotifier(widget.outsidePm10), // ✅ 고침,
                          builder: (context, outVal, _) {
                            return DustCompareCard(
                              homeName: widget.homeName!,
                              homeStatus: getPM25Status(homeVal),
                              outsideStatus: getPM25Status(outVal),
                              homeLabel: "${widget.homeName} 초미세먼지",
                              outLabel: "${widget.zone} 초미세먼지",
                              name: "초미세먼지",
                              onHomeNameChanged: widget.onHomeNameChanged,
                            );
                          },
                        );
                      },
                    ),
                  ],
                ),
                Positioned(
                  bottom: 8,
                  right: 8,
                  child: Row(
                    children: [
                      _buildDot(0),
                      SizedBox(width: 6.w),
                      _buildDot(1),
                    ],
                  ),
                ),
              ],
            ),
          )
        ],
      ),
    );
  }
}
