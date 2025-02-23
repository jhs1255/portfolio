package view;

import java.util.ArrayList;
import java.util.HashMap;

public class AppData {
    private static final HashMap<String, ArrayList<String>> movieScreenings = new HashMap<>();

    public static HashMap<String, ArrayList<String>> getMovieScreenings() {
        return movieScreenings;
    }
    
    // 영화에 상영 정보를 추가하는 메서드
    public static void addMovieScreening(String title, String screeningInfo) {
        movieScreenings.computeIfAbsent(title, k -> new ArrayList<>()).add(screeningInfo);
    }
}
