package com.example.LABA4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidConf WidConf}
 */
public class Wid extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WidConf.getPref(context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try
        {
            date = sd.parse(widgetText.toString());
            if (System.currentTimeMillis() > date.getTime() )
            {
                widgetText = "Событие уже наступило!";
                if(currentDate.getTime() == 9) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setContentText("Событие наступило!")
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setSmallIcon(R.mipmap.ic_launcher);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(1234, builder.build());
                }
            }
            else
                {
                long diftime = date.getTime() - System.currentTimeMillis();
                long differenceDays = ( diftime / (1000 * 60 * 60 * 24) ) + 1;
                widgetText = "Дней до события: " + differenceDays;
            }
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent configIntent = new Intent(context, WidConf.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId,
                configIntent, 0);
        views.setOnClickPendingIntent(R.id.mainLayer, pIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            WidConf.clearPref(context, appWidgetId);
        }
    }
}

