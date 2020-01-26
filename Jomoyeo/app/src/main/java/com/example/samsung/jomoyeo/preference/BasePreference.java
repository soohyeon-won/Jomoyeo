package com.example.samsung.jomoyeo.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 */
public class BasePreference {
    private final String mPreferencesName;

    protected BasePreference(String preferencesName) {
        mPreferencesName = preferencesName;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////						값 저장을 위한 메소드								////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected synchronized SharedPreferences getPreferences(Context context) {

        return context.getApplicationContext().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
    }


    protected synchronized void setValue(Context context, String key, String value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    protected synchronized void setValue(Context context, String key, boolean value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    protected synchronized void setValue(Context context, String key, int value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    protected synchronized void setValue(Context context, String key, long value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    protected synchronized void setValue(Context context, String key, float value) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putFloat(key, value);
        editor.commit();
    }

    protected synchronized void removeValue(Context context, String key) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(key);
        editor.commit();
    }


    protected synchronized void setObject(Context context, String name, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        clearObject(context, name);

        if (object == null) {
            return;
        }

        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
        } catch (Exception e) {
//			Trace.Error("save fail: " + e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected synchronized Object getObject(Context context, String name) {
        Object object = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(name);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();

        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                }
            }
        }
        return object;
    }

    protected synchronized void clearObject(Context context, String name) {
        context.deleteFile(name);
    }
}
