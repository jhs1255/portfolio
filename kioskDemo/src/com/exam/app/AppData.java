package com.exam.app;

import java.util.ArrayList;
import java.util.HashMap;

public class AppData {
    private static final HashMap<String, ArrayList<String>> movieScreenings = new HashMap<>();

    public static HashMap<String, ArrayList<String>> getMovieScreenings() {
        return movieScreenings;
    }
}
