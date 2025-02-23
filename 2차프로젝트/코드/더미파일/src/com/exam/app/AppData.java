package com.exam.app;

import java.util.ArrayList;
import java.util.HashMap;

public class AppData {

    // 영화 상영 정보를 저장하는 HashMap (title -> 상영 정보 리스트)
    private static HashMap<String, ArrayList<HashMap<String, String>>> movieScreenings = new HashMap<>();

    // 하드코딩된 영화 데이터
    public static void loadMovieScreenings() {
        // 첫 번째 영화 상영 정보 추가
        String movieTitle1 = "The Avengers";
        ArrayList<HashMap<String, String>> screenings1 = new ArrayList<>();
        HashMap<String, String> screeningInfo1 = new HashMap<>();
        screeningInfo1.put("date", "11/21");
        screeningInfo1.put("theater", "2관");
        screeningInfo1.put("time", "13:30");
        screenings1.add(screeningInfo1);

        HashMap<String, String> screeningInfo2 = new HashMap<>();
        screeningInfo2.put("date", "11/22");
        screeningInfo2.put("theater", "3관");
        screeningInfo2.put("time", "15:30");
        screenings1.add(screeningInfo2);

        movieScreenings.put(movieTitle1, screenings1);

        // 두 번째 영화 상영 정보 추가
        String movieTitle2 = "Inception";
        ArrayList<HashMap<String, String>> screenings2 = new ArrayList<>();
        HashMap<String, String> screeningInfo3 = new HashMap<>();
        screeningInfo3.put("date", "11/23");
        screeningInfo3.put("theater", "1관");
        screeningInfo3.put("time", "17:30");
        screenings2.add(screeningInfo3);

        HashMap<String, String> screeningInfo4 = new HashMap<>();
        screeningInfo4.put("date", "11/24");
        screeningInfo4.put("theater", "2관");
        screeningInfo4.put("time", "19:30");
        screenings2.add(screeningInfo4);

        movieScreenings.put(movieTitle2, screenings2);
    }

    // 영화의 상영 종류를 하드코딩된 값으로 반환
    public static String getMovieType(String title) {
        switch (title) {
            case "The Avengers":
                return "2D";
            case "Inception":
                return "IMAX";
            default:
                return "Unknown";
        }
    }

    // 영화의 관람가를 하드코딩된 값으로 반환
    public static String getMovieRating(String title) {
        switch (title) {
            case "The Avengers":
                return "전체 관람가";
            case "Inception":
                return "15세 이상 관람가";
            default:
                return "Unknown";
        }
    }

    // 영화의 러닝타임을 하드코딩된 값으로 반환
    public static String getMovieRuntime(String title) {
        switch (title) {
            case "The Avengers":
                return "143분";
            case "Inception":
                return "148분";
            default:
                return "Unknown";
        }
    }

    // 상영 정보 가져오기
    public static HashMap<String, ArrayList<HashMap<String, String>>> getMovieScreenings() {
        return movieScreenings;
    }

    // 영화에 상영 정보를 추가하는 메서드
    public static void addMovieScreening(String title, HashMap<String, String> screeningInfo) {
        movieScreenings.computeIfAbsent(title, k -> new ArrayList<>()).add(screeningInfo);
    }
}
