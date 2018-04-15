package com.imnotpayingforthat.imnotpayingforthat.util;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Globals {
    public static final String dbCollection_user = "users";
    public static enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public static final String SHARED_PREF = "NOTPAYINGPREFS";
    public static final String TEAM_ID_KEY = "TEAMIDKEY";

    private static String selectedTeamId;

    public static void setSelectedTeamId(String id) {
        selectedTeamId = id;
    }

    public static String getSelectedTeamId() {
        return selectedTeamId;
    }

    private static List<String> colors = Arrays.asList("#9FA8DA", "#80DEEA", "#C5E1A5", "#FFF59D", "#B0BEC5");

    public static int colorCounter = 0;
    public static int getNextColor() {
        int newcolor = Color.parseColor(colors.get(colorCounter % colors.size()));
        colorCounter++;
        return newcolor;
    }
}
