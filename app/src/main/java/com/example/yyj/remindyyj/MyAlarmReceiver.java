package com.example.yyj.remindyyj;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yyj on 2018/3/13.
 */

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("com.example.yyj.remindyyj.ALARM".equals(intent.getAction())) {

            Log.d("ring","now begin to ring");
            Intent intent1 = new Intent(context, RingActivity.class);

            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent1);
        }
    }
}
