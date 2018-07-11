package com.demo.dynamiclocalizationsdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.demo.dynamiclocalizationsdemo.localization.LocalizationAPI;
import com.demo.dynamiclocalizationsdemo.views.DashboardActivity;
import com.demo.dynamiclocalizationsdemo.views.LoginActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    Locale currentLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        currentLocale = Locale.getDefault();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!currentLocale.equals(newConfig.locale)) {
            currentLocale = newConfig.locale;
            LocalizationAPI.sharedInstance().initialize();
            Intent intent = new Intent(this, LoginActivity.class);
            if (isUserLoggedIn()) {
                intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
        }
    }

    private boolean isUserLoggedIn() {
        return true;
    }
}
