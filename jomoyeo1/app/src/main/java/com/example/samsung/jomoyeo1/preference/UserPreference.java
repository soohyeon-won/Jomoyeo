package com.example.samsung.jomoyeo1.preference;

import android.content.Context;

public class UserPreference extends BasePreference {

    private static final UserPreference INSTANCE = new UserPreference();
    private static final String PREFERENCE_NAME = "UserPreference";
    private static final String ID = "ID";

    private UserPreference() {
        super(PREFERENCE_NAME);
    }

    public static UserPreference getInstance() {
        return INSTANCE;
    }

    /* id 저장 */
    public void setId(Context context, String id) {
        setValue(context, ID, id);
    }
    public String getId(Context context) {
        return getPreferences(context).getString(ID, "-1");
    }


}
