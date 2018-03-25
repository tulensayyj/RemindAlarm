package com.example.yyj.remindyyj;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {


    int id;
    long now;
    long c;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intentHeigh=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intentHeigh,0);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("Alarm Clock")
                .setContentText("Alarm is working")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_alarm_on_black_24dp))
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(intent.getLongArrayExtra("Data")==null) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Intent intent1 = new Intent(MyService.this, RingActivity.class);

                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    MyService.this.startActivity(intent1);

                }
            }).start();
        } else {


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long[] Data = intent.getLongArrayExtra("Data");

            id = (int) Data[0];
            now = Data[1];
            c = Data[2];

            Intent intentAlarm = new Intent(this, MyService.class);
       /* intentAlarm.setAction("com.example.yyj.remindyyj.ALARM");

        PendingIntent pendingIntent=PendingIntent.getBroadcast(MyService.this,id,
                intentAlarm,PendingIntent.FLAG_UPDATE_CURRENT);*/

            PendingIntent pendingIntent = PendingIntent.getService(this, id, intentAlarm, 0);


            Log.d("boardcast", "now begin to id" + "=" + id);
            Log.d("boardcast", "now begin to boardcast" + "=" + c);
            Log.d("boardcast", "now begin to boardcast" + "=" + System.currentTimeMillis());

                /* 这里的比较有疑问 c.getTimeInMillis和 system.current...应该是相等的 */

            if (c < System.currentTimeMillis()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c + 24 * 60 * 60 * 1000, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c, pendingIntent);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
