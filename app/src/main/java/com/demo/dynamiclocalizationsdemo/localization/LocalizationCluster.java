package com.demo.dynamiclocalizationsdemo.localization;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * Created by Rahul on 8/24/2016.
 */
public class LocalizationCluster {

    @SerializedName("default")
    @Expose
    private String mDefaultLanguage;

    @SerializedName("languagePack")
    @Expose
    private List<Localization> mLocalizations;

    public static LocalizationCluster makeFromInputStream(InputStream inputStream) {
        LocalizationCluster ret = null;
        try {
            if (inputStream != null) {
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(inputStream);
                ret = gson.fromJson(reader, LocalizationCluster.class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ret;
    }

    public static LocalizationCluster makeFromJSON(String json) {
        return json != null ? new Gson().fromJson(json, LocalizationCluster.class) : null;
    }

    public Localization defaultPack() {
        return packForLocale(mDefaultLanguage);
    }

    public Localization packForLocale(String locale) {

        Localization localization = null;
        if (mLocalizations != null) {
            for (Localization pack : mLocalizations) {
                if (pack.getLocales() != null && pack.getLocales().contains(locale)) {
                    localization = pack;
                    break;
                }
            }
        }
        return localization;
    }
}