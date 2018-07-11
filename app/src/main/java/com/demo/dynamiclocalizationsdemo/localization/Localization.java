package com.demo.dynamiclocalizationsdemo.localization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Rahul on 8/17/2016.
 */
public class Localization {

    @SerializedName("locales")
    @Expose
    private List<String> mLocales;

    @SerializedName("text")
    @Expose
    private Map<String, Object> mLocalizationMap;

    public List<String> getLocales() {
        return this.mLocales;
    }

    public String getLocalizedString(String key) {

        String localizedString = key;

        if (mLocalizationMap != null)
            localizedString = (String) mLocalizationMap.get(key);

        return localizedString;
    }
}
