package com.example.LABA4;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;


public class WidConf extends Activity {
    private static final String PREFS_NAME = "com.example.lab4.Wid";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // настройка виджета
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_configure);
        WidText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        // настройка календаря
      DataSet();
    }


    EditText WidText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidConf.this;
            String widgetText = WidText.getText().toString();
            //saveTitlePref(context, mAppWidgetId, widgetText);

            SharedPreferences.Editor pr = context.getSharedPreferences(PREFS_NAME, 0).edit();
            pr.putString(PREF_PREFIX_KEY + mAppWidgetId, widgetText);
            pr.apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Wid.updateAppWidget(context, appWidgetManager, mAppWidgetId);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


    public void DataSet()
    {
        CalendarView calendarView = findViewById(R.id.calendarView);
        if (calendarView != null) {
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String msg = "" + dayOfMonth + "/" + (month + 1) + "/" + year;
                    WidText.setText(msg);
                }
            });
        }
        SharedPreferences prefs = WidConf.this.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + mAppWidgetId, null);
        WidText.setText(titleValue);
    }


    static String getPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void clearPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

}

