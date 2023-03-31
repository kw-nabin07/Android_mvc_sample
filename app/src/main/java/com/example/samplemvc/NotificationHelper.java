package com.example.samplemvc;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.example.samplemvc.model.bean.ToDo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "My Notification Channel";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "This is my notification channel";

    public static void scheduleNotification(Context context, List<ToDo>toDoList) {
        Log.d(TAG,"scheduleNotification is called from : "+context.toString());
            try{
                List<ToDo>notificationTodos;
                notificationTodos = toDoList;
                for(ToDo todos : notificationTodos){
                    int notificationStatus = todos.getNotificationStatus();
                    int notificationId = (int)todos.getId();
                    String title = todos.getTitle();
                    Date date = getDate(todos.getDate(),todos.getTime());
                    Calendar calendar = Calendar.getInstance();
                    //30分以上のタスクのみ通知設定するため
                    long nowDate = (calendar.getTimeInMillis() + 30 * 60 * 1000);
                    calendar.setTime(date);
                    long itemDate = calendar.getTimeInMillis();
                    if(notificationStatus == 1 && nowDate < itemDate){
                        notificationForComingTask(context,notificationId,itemDate,title);
                    }else {
                        Log.d(TAG,"通知設定しているタスクがございません。");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }
    //今後のタスクの通知設定
    private static void notificationForComingTask(Context context,int notificationId, Long notificationTime,String title){
        Log.d(TAG,"notificationForComingTask.今後のタスクの通知設定");
        // Create a pending intent to launch the notification when the alarm goes off
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("message", "今後のタスク："+title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,PendingIntent.FLAG_IMMUTABLE);
        // Get the alarm manager service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Set the alarm to go off at the given date and time

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, (notificationTime - 30 * 60 * 1000), pendingIntent);
        // Create a notification channel for the notification
        createNotificationChannel(context);
    }

    public static Date getDate(String date, String time){
        Date formattedDate=null;
        String formattedTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        try{
            Date dateTime = sdf.parse(time);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            formattedTime = sdf1.format(dateTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String dateTime = date+" "+formattedTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            formattedDate = sdf1.parse(dateTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return formattedDate;
    }

    static void createNotificationChannel(Context context) {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG,"createNotificationChannel is called.");
        }
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the message from the intent
            String message = intent.getStringExtra("message");

            Intent intentForNext = new Intent(context, ShowAllToDoActivity.class);
            intentForNext.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentForNext,PendingIntent.FLAG_IMMUTABLE);

            Log.d("Notification Receiver","is running");
            // Build the notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Todo Application")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_notice)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            // Display the notification
            int notificationId = new Random().nextInt();
            notificationManager.notify(notificationId, notification);
        }
    }
}

