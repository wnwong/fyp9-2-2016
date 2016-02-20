package com.example.user.secondhandtradingplatform;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by User on 20/2/2016.
 */
public class customHandler extends BroadcastReceiver {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TAG = "customHandler";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);
        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            //Check for Pushbots Instance
            Pushbots pushInstance = Pushbots.sharedInstance();
            if (!pushInstance.isInitialized()) {
                Log.d(TAG, "Initializing Pushbots.");
                Pushbots.sharedInstance().init(context.getApplicationContext());
            }

            //Clear Notification array
            if (PBNotificationIntent.notificationsArray != null) {
                PBNotificationIntent.notificationsArray = null;
            }

            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
            Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("message"));

            //Report Opened Push Notification to Pushbots
            if (Pushbots.sharedInstance().isAnalyticsEnabled()) {
                Pushbots.sharedInstance().reportPushOpened((String) PushdataOpen.get("PUSHANALYTICS"));
            }

            //Start lanuch Activity
            String packageName = context.getPackageName();
            Intent resultIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage(packageName));
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            resultIntent.putExtras(intent.getBundleExtra("pushData"));
            Pushbots.sharedInstance().startActivity(resultIntent);

            // Handle Push Message when received
        } else if (action.equals(PBConstants.EVENT_MSG_RECEIVE)) {
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            Log.w(TAG, "User Received notification with Message: " + PushdataOpen.get("message"));
            Log.w(TAG, "User Received notification with Message: " + PushdataOpen.get("seller"));
            Date mDate = null;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.putExtra("seller", (String) PushdataOpen.get("seller"));
            notificationIntent.putExtra("time", (String) PushdataOpen.get("time"));
            notificationIntent.putExtra("date", (String) PushdataOpen.get("date"));
            notificationIntent.putExtra("location", (String) PushdataOpen.get("location"));
            notificationIntent.putExtra("product", (String) PushdataOpen.get("product"));

            PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                mDate = simpleDateFormat.parse((String) PushdataOpen.get("notiDate"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(mDate);
            Log.w(TAG, "NotiDate: " + PushdataOpen.get("date"));
            Log.w(TAG,"Location: " + PushdataOpen.get("location"));
            Log.w(TAG, "Time: " + PushdataOpen.get("time"));
            Log.w(TAG, mDate.toString());
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }
    }

}