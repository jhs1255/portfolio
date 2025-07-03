import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pi_project/utils/if_status.dart';

class DustCompareCard extends StatefulWidget {
  final DustStatus homeStatus;
  final DustStatus outsideStatus;
  final String homeName;
  final String homeLabel;
  final String outLabel;
  final String name;
  final ValueChanged<String> onHomeNameChanged;

  const DustCompareCard({
    Key? key,
    required this.homeName,
    required this.homeStatus,
    required this.outsideStatus,
    required this.homeLabel,
    required this.outLabel,
    required this.name,
    required this.onHomeNameChanged,
  }) : super(key: key);

  @override
  State<DustCompareCard> createState() => _DustCompareCardState();
}

class _DustCompareCardState extends State<DustCompareCard> {
  late TextEditingController _controller;
  bool isEditing = false;

  @override
  void initState() {
    super.initState();
    _controller = TextEditingController(text: widget.homeName);
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  String _filterInput(String input) {
    final regExp = RegExp(r'^[가-힣\s]{0,4}$');
    return regExp.hasMatch(input) ? input : _controller.text;
  }

  void _saveName() {
    final filtered = _filterInput(_controller.text);
    widget.onHomeNameChanged(filtered);
    setState(() {
      _controller.text = filtered;
      isEditing = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;

    return Container(
      width: 300.w,
      height: 330.h,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(20),
      ),
      padding: const EdgeInsets.all(12),
      child: Column(
        children: [
          // ✅ 위쪽 두 개 비교 박스
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              _buildStatusBox(context, widget.homeLabel, widget.homeStatus),
              _buildStatusBox(context, widget.outLabel, widget.outsideStatus),
            ],
          ),
          SizedBox(height: 15.0.h),

          // ✅ 하단 설명 문구 + 편집 영역
          Container(
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 12.h),
            decoration: BoxDecoration(
              color: widget.homeStatus.color.withOpacity(0.1),
              borderRadius: BorderRadius.circular(12.r),
            ),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    isEditing
                        ? SizedBox(
                      width: 100.w,
                      height: 35.h,
                      child: TextField(
                        controller: _controller,
                        maxLength: 5,
                        maxLines: 1,
                        style: TextStyle(fontSize: 15.sp),
                        decoration: InputDecoration(
                          counterText: '',
                          contentPadding: EdgeInsets.symmetric(horizontal: 8.w),
                          isDense: true,
                          border: OutlineInputBorder(),
                        ),
                        onChanged: (val) {
                          final filtered = _filterInput(val);
                          if (filtered != _controller.text) {
                            _controller.value = TextEditingValue(
                              text: filtered,
                              selection: TextSelection.collapsed(offset: filtered.length),
                            );
                          }
                        },
                      ),
                    )
                        : Text(
                      _controller.text,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: screenWidth <= 360 ? 15.sp : 17.sp,
                      ),
                    ),
                    SizedBox(width: 6.w),
                    isEditing
                        ? GestureDetector(
                      onTap: _saveName,
                      child: Icon(Icons.check, size: 22.sp, color: Colors.green),
                    )
                        : GestureDetector(
                      onTap: () => setState(() => isEditing = true),
                      child: Text(" ✏️", style: TextStyle(fontSize: 12.sp)),
                    ),
                  ],
                ),
                SizedBox(height: 8.h),
                Text(
                  '${widget.name}는 ${widget.homeStatus.level}입니다.',
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: screenWidth <= 360 ? 13.sp : 15.sp),
                ),
                SizedBox(height: 6.h),
                Text(
                  widget.homeStatus.Home_description ?? '',
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: screenWidth <= 360 ? 12.sp : 15.sp),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatusBox(BuildContext context, String label, DustStatus status) {
    final screenWidth = MediaQuery.of(context).size.width;

    return Column(
      children: [
        Text(label, style: TextStyle(fontWeight: FontWeight.bold, fontSize: screenWidth <= 360 ? 14.sp : 17.sp)),
        SizedBox(height: 6.h),
        CircleAvatar(
          radius: 30,
          backgroundColor: status.color.withOpacity(0.1),
          child: Text(status.emoji, style: TextStyle(fontSize: 35.sp)),
        ),
        SizedBox(height: 6.h),
        Text(status.level, style: TextStyle(color: status.color, fontSize: screenWidth <= 360 ? 15.sp : 17.sp)),
        Text("${status.value}㎍/㎥", style: TextStyle(fontSize: screenWidth <= 360 ? 15.sp : 17.sp)),
      ],
    );
  }
}
