package com.imnotpayingforthat.imnotpayingforthat.util;

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

}
