package com.demo.dynamiclocalizationsdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.dynamiclocalizationsdemo.DemoApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahulhariharan on 11/07/18.
 */

public class SharedPreferenceUtil {

    private static SharedPreferenceUtil sharedInstance = new SharedPreferenceUtil();

    private SharedPreferenceUtil() {
    }

    public static SharedPreferenceUtil sharedInstance() {
        return sharedInstance;
    }

    private static final String KEY_APPLICATION_DEFAULTS = "KEY_APPLICATION_DEFAULTS";

    public void removeData(String dataKey) {
        try {
            removeDataSet(new String[]{dataKey});
        } catch (Exception ex) {

        }
    }

    public void removeDataSet(String[] keys) {
        try {
            SharedPreferences.Editor editor = DemoApplication.getContext().getSharedPreferences(KEY_APPLICATION_DEFAULTS, Context.MODE_PRIVATE).edit();
            for (String key : keys) {
                editor.remove(key);
            }

            editor.commit();
        } catch (Exception ex) {

        }
    }

    public void setDataKey(String key, String value) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(key, value);
            setDataSet(map);
        } catch (Exception ex) {
            //no-op
        }
    }

    public void setDataSet(Map<String, String> map) {
        try {
            SharedPreferences.Editor editor = DemoApplication.getContext().getSharedPreferences(KEY_APPLICATION_DEFAULTS, Context.MODE_PRIVATE).edit();
            for (String key : map.keySet()) {
                editor.putString(key, map.get(key));
            }

            editor.commit();
        } catch (Exception ex) {

        }
    }

    public String getData(String key) {
        return DemoApplication.getContext().getSharedPreferences(KEY_APPLICATION_DEFAULTS, Context.MODE_PRIVATE).getString(key, null);
    }
}
