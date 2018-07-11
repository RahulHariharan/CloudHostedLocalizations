package com.demo.dynamiclocalizationsdemo.localization;

import android.util.Log;

import com.demo.dynamiclocalizationsdemo.DemoApplication;
import com.demo.dynamiclocalizationsdemo.utils.FileUtil;
import com.demo.dynamiclocalizationsdemo.R;
import com.demo.dynamiclocalizationsdemo.utils.SharedPreferenceUtil;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URI;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Rahul on 8/17/2016.
 */
public class LocalizationAPI {

    //Dynamic
    private final String DYNAMIC_PACK_FILE_PATH = "/dynamic_suite.json";
    private final String KEY_DYNAMIC_PACK_URL = "DYNAMIC_PACK_URL";
    private Localization mDynamicLocalization;
    private String mDynamicLanguageSuiteURL;

    //End-Dynamic

    private Localization mDefaultLocalization;
    private static LocalizationAPI mInstance;

    private LocalizationAPI() {
    }

    public static LocalizationAPI sharedInstance() {

        if (mInstance == null) {
            mInstance = new LocalizationAPI();
        }

        return mInstance;
    }

    public void initialize() {
        //default pack
        InputStream inputStream = DemoApplication.getContext().getResources().openRawResource(R.raw.localizations_default);
        LocalizationCluster localizationCluster = LocalizationCluster.makeFromInputStream(inputStream);
        mDefaultLocalization = getLangPackFromSuite(localizationCluster);

        //dynamic pack
        String strDynamicSuite = FileUtil.readString(DYNAMIC_PACK_FILE_PATH);
        LocalizationCluster dynamicSuite = LocalizationCluster.makeFromJSON(strDynamicSuite);
        mDynamicLocalization = getLangPackFromSuite(dynamicSuite);

        //dynamic pack url
        mDynamicLanguageSuiteURL = SharedPreferenceUtil.sharedInstance().getData(KEY_DYNAMIC_PACK_URL);
    }

    private Localization getLangPackFromSuite(LocalizationCluster suite) {
        Localization ret = null;
        if (suite != null) {
            String currentLocale = Locale.getDefault().toString();
            ret = suite.packForLocale(currentLocale);

            if (ret == null) {
                String currentLanguage = Locale.getDefault().getLanguage();
                ret = suite.packForLocale(currentLanguage);
            }

            if (ret == null)
                ret = suite.defaultPack();
        }
        return ret;
    }

    private void setDynamicPack(Localization pack, String withJSON) {
        mDynamicLocalization = pack;
        if (pack != null && withJSON != null) {
            FileUtil.writeString(DYNAMIC_PACK_FILE_PATH, withJSON);
        }
        else {
            FileUtil.removeFile(DYNAMIC_PACK_FILE_PATH);
        }
    }

    public String getString(String key) {
        String localizedValue = null;

        if (mDynamicLocalization != null)
            localizedValue = mDynamicLocalization.getLocalizedString(key);

        if (localizedValue == null && mDefaultLocalization != null)
            localizedValue = mDefaultLocalization.getLocalizedString(key);

        if (localizedValue == null)
            localizedValue = key;

        return localizedValue;
    }

    /*
    Dynamic Language Pack Loadig
    * */
    public void loadDynamicLanguagePack(String url, LanguagePackReadyListener callback) {
        boolean changed = (null == url) || (!url.equals(mDynamicLanguageSuiteURL));
        mDynamicLanguageSuiteURL = url;
        if (mDynamicLanguageSuiteURL != null) {
            SharedPreferenceUtil.sharedInstance().setDataKey(KEY_DYNAMIC_PACK_URL, mDynamicLanguageSuiteURL);
        } else {
            SharedPreferenceUtil.sharedInstance().sharedInstance().removeData(KEY_DYNAMIC_PACK_URL);
        }

        if (changed) {
            setDynamicPack(null, null);
            fetchDynamicPack(callback);
        } else {
            if (mDynamicLanguageSuiteURL != null && mDynamicLocalization == null) {
                //Pack could not be loaded earlier.. try again
                fetchDynamicPack(callback);
            } else if (callback != null) {
                callback.onLanguagePackAvailable();
            }
        }
    }

    interface LanguagePackAPI {
        @GET("v0/b/dynamiclocalizationsdemo.appspot.com/o/document.json?alt=media&token=6321f768-26e6-4523-bf18-5e32bcea0b9a")
        Call<LocalizationCluster> get();
    }

    private void fetchDynamicPack(final LanguagePackReadyListener readyCallback) {
        if (mDynamicLanguageSuiteURL != null) {
            try {
                URI uri = new URI(mDynamicLanguageSuiteURL);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://firebasestorage.googleapis.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LanguagePackAPI service = retrofit.create(LanguagePackAPI.class);
                Call<LocalizationCluster> call = service.get();
                call.enqueue(new Callback<LocalizationCluster>() {
                    @Override
                    public void onResponse(Call<LocalizationCluster> call, Response<LocalizationCluster> response) {
                        Log.v("event_", response.body().toString());
                        LocalizationCluster localizationCluster = response.body();
                        Localization pack = getLangPackFromSuite(localizationCluster);
                        String jsonSuite = pack != null ? new Gson().toJson(localizationCluster).toString() : null;
                        setDynamicPack(pack, jsonSuite);
                        if (readyCallback != null) {
                            readyCallback.onLanguagePackAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<LocalizationCluster> call, Throwable t) {
                        Log.v("event_", "exception1");
                        t.printStackTrace();
                    }
                });

            } catch(Exception e) {
                setDynamicPack(null, null);
                e.printStackTrace();
                if (readyCallback != null) {
                    readyCallback.onLanguagePackAvailable();
                }
            }
        } else {
            Log.v("event_", "exception3");
            setDynamicPack(null, null);
            if (readyCallback != null) {
                readyCallback.onLanguagePackAvailable();
            }
        }
    }

    /*
    End Dynmaic Pack Loading
    * */
    public interface LanguagePackReadyListener {
        void onLanguagePackAvailable();
    }
}