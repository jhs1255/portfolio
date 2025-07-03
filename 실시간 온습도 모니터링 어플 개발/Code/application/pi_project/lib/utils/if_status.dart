import 'package:flutter/material.dart';

/// 미세먼지 상태 정보 클래스
class DustStatus {
  final String level;        // 등급 이름
  final String emoji;        // 이모지
  final Color color;         // 이모지 색상
  final String? Home_description;    // 우리집 전용 문구
  final String? Outside_description; // 밖 공기 전용 문구// 설명 문구
  final int? value;

  const DustStatus({
    required this.level,
    required this.emoji,
    required this.color,
    this.Home_description,
    this.Outside_description,
    this.value,
  });
}

/// 미세먼지(PM10) 수치 → 상태 객체 리턴
DustStatus getPM10Status(int pm10) {

  if (pm10 < 0) {
    return DustStatus(
      level: '측정할 수 없음',
      emoji: '😶',
      color: Colors.grey,
      Outside_description: '값이 측정되지 못하고 있어요.',
      Home_description: '값이 측정되지 못하고 있어요.',
      value: pm10,
    );
  }
  else if (pm10 <= 30) {
    return DustStatus(
      level: '좋음',
      emoji: '😊',
      color: Colors.blue,
      Outside_description: '밖 공기가 아주 맑아요!\n지금 같은 날은 산책하기 최고예요~ 😊',
      Home_description: '공기가 맑고 깨끗해요!\n지금 우리집은 청정구역이에요!\n상쾌하게 숨 쉬세요~ 😊',
      value: pm10,
    );
  }
  else if (pm10<=0) {
    return DustStatus(
      level: 'x',
      emoji: '😶',
      color: Colors.green,
      Outside_description: '밖 공기는 보통 수준이에요.\n가벼운 외출엔 문제 없어요~ 🙂',
      Home_description: '실내 공기 상태가 무난해요.\n쾌적한 하루 보내세요~ 🙂',
      value: pm10,
    );
  }
  else if (pm10 <= 80) {
    return DustStatus(
      level: '보통',
      emoji: '🙂',
      color: Colors.green,
      Outside_description: '밖 공기는 보통 수준이에요.\n가벼운 외출엔 문제 없어요~ 🙂',
      Home_description: '실내 공기 상태가 무난해요.\n쾌적한 하루 보내세요~ 🙂',
      value: pm10,
    );
  } else if (pm10 <= 150) {
    return DustStatus(
      level: '나쁨',
      emoji: '😷',
      color: Colors.red,
      Outside_description: '밖 공기가 탁해요.\n외출 시 마스크 꼭 착용하세요! 😷',
      Home_description: '실내 공기가 조금 답답하게 느껴질 수 있어요.\n환기나 공기청정기를 고려해보세요. 😷',
      value: pm10,
    );
  } else {
    return DustStatus(
      level: '매우 나쁨',
      emoji: '😵',
      color: Color(0xFF8B0000),
      Outside_description: '지금은 외출을 삼가야 해요.\n공기 오염도가 매우 높습니다! 😵',
      Home_description: '실내 공기 상태가 매우 좋지 않아요.\n가급적 환기를 자제하고, 공기청정기 가동을 권장해요. 😵',
      value: pm10,
    );
  }
}

/// 초미세먼지(PM2.5) 수치 → 상태 객체 리턴
DustStatus getPM25Status(int pm25) {
  if (pm25<0) {
    return DustStatus(
        level: '측정할 수 없음',
        emoji: '😶',
        color: Colors.grey,
        Outside_description: '값이 측정되지 못하고 있어요.',
        Home_description: '값이 측정되지 못하고 있어요.',
        value: pm25,
    );
  }
  else if (pm25 <= 15) {
    return DustStatus(
      level: '좋음',
      emoji: '😊',
      color: Colors.blue,
      Outside_description: '밖 공기가 아주 맑아요!\n숨쉬기 정말 좋은 날이에요~ 😊',
      Home_description: '실내 공기가 매우 맑아요.\n이럴 땐 기분까지 상쾌하죠! 😊',
      value: pm25,
    );
  } else if (pm25 <= 35) {
    return DustStatus(
      level: '보통',
      emoji: '😐',
      color: Colors.green,
      Outside_description: '밖 공기는 무난한 편이에요.\n외출하셔도 괜찮아요~ 🙂',
      Home_description: '우리집 공기는 보통이에요.\n조금은 정체된 느낌이 있지만 \n 크게 불편하진 않아요.🙂',
      value: pm25,
    );
  } else if (pm25 <= 75) {
    return DustStatus(
      level: '나쁨',
      emoji: '😷',
      color: Colors.red,
      Outside_description: '밖 공기 질이 안 좋아요.\n외출 시 마스크 꼭 챙기세요! 😷',
      Home_description: '공기 상태가 좋지 않아요.\n공기청정기를 켜는 게 좋겠어요 😷',
      value: pm25,
    );
  }
  else {
    return DustStatus(
      level: '매우 나쁨',
      emoji: '😵',
      color: Color(0xFF8B0000),
      Outside_description: '초미세먼지가 심각해요!\n되도록 외출을 피해주세요 😵',
      Home_description: '숨쉬기 답답하고 불편할 수 있어요.\n공기청정기 사용이 꼭 필요해 보여요. 😵',
      value: pm25,
    );
  }
}
DustStatus getStatusFromLevel(String level) {
  switch (level.trim()) {
    case '좋음':
      return getPM10Status(30);
    case '보통':
      return getPM10Status(50);
    case '나쁨':
      return getPM10Status(100);
    case '매우 나쁨':
      return getPM10Status(200);
    default:
      return DustStatus(
        level: '정보 없음',
        emoji: '❓',
        color: Colors.grey,
        Outside_description: '정보 없음',
        Home_description: '정보 없음',
        value: null,
      );
  }
}

