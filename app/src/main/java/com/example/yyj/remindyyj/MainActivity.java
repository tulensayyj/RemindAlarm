package com.example.yyj.remindyyj;

import android.app.AlarmManager;

import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import android.widget.Toast;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private MyAdapter myAdapter;
    private int mRecycleViewHight;
    private MyDecoration myDecoration;

    /*private AlarmManager alarmManager;
    private Calendar calendar;*/

    private SQLiteDatabase mdbWriter;
    private SQLiteDatabase mdbReader;
    private MySQLite mySQLite;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView =(RecyclerView) findViewById(R.id.MyRecycleView);

        mRecycleViewHight=mRecyclerView.getHeight();



        /*mySQLite=new MySQLite(getApplicationContext());*/

        iniData();

        myAdapter = new MyAdapter(MainActivity.this,mDatas,mRecycleViewHight);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mRecyclerView.setAdapter( myAdapter);

        //设置分隔线，自己重写的
        myDecoration=new MyDecoration(this,layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(myDecoration);


        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(final int position) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("提示")
                        .setMessage("是否确定删除该闹钟?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteAlarms(position);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "添加闹钟", Snackbar.LENGTH_LONG)
                        .setAction("set the time", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setClock();
                            }
                        }).show();
            }
        });
    }

    private void iniData(){
        mDatas=new ArrayList<String>();


        mySQLite=new MySQLite(getApplicationContext());


        mdbReader=mySQLite.getReadableDatabase();

        cursor = mdbReader.query("Alarms",null,null,null,null,null,null);

        while (cursor.moveToNext()){
            String strTemp=cursor.getString(cursor.getColumnIndex("Time"));
            mDatas.add(strTemp);
        }

        cursor.close();
        mdbReader.close();
        mySQLite.close();
    }

    public void setClock(){

        Log.i("Alarm","打开对话框");

        Calendar calendar=Calendar.getInstance();

        Dialog dialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)   //API 19以上的版本
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c=Calendar.getInstance();


                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);        //设置闹钟小时数
                c.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
                if(hourOfDay < 12) {
                   c.set(Calendar.AM_PM,Calendar.AM);
                } else {
                    c.set(Calendar.AM_PM,Calendar.PM);
                }

                int id=saveAlarms(hourOfDay,minute);

                Intent startAlarmService=new Intent(MainActivity.this,MyService.class);

                startAlarmService.putExtra("Data",new long[]{id,System.currentTimeMillis(),c.getTimeInMillis()});

                startService(startAlarmService);

                /*Intent intent=new Intent();
                intent.setAction("com.example.yyj.remindyyj.ALARM");

                PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,id,
                        intent,PendingIntent.FLAG_UPDATE_CURRENT);

                Log.d("boardcast","now begin to boardcast"+"="+c.getTimeInMillis());
                Log.d("boardcast","now begin to boardcast"+"="+System.currentTimeMillis());
                /* 这里的比较有疑问 c.getTimeInMillis和 system.current...应该是相等的 */
                /*if(c.getTimeInMillis()<System.currentTimeMillis()){
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+24*60*60*1000,pendingIntent);

                }
                else{
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

                }*/




            }
        },calendar.get(Calendar.HOUR_OF_DAY)
         ,calendar.get(Calendar.MINUTE)
         ,true);
        dialog.show();
    }

    public int saveAlarms(int hourOfDay,int minute){


        Log.i("Alarm","写入数据库");



        mySQLite=new MySQLite(getApplicationContext());


        mdbWriter=mySQLite.getWritableDatabase();
        mdbReader=mySQLite.getReadableDatabase();

        cursor = mdbReader.query("Alarms",null,null,null,null,null,null);

        String strMinute;
        if(minute<=9){
            strMinute="0"+Integer.toString(minute);
        }
        else{
            strMinute=Integer.toString(minute);
        }
        String strSave=Integer.toString(hourOfDay)+":"+strMinute;
        ContentValues contentValues=new ContentValues();

        contentValues.put("Time",strSave);

        mdbWriter.insert("Alarms", null, contentValues);

        mDatas.add(strSave);
        myAdapter.notifyItemChanged(myAdapter.getItemCount()-1);

        cursor.moveToLast();
        int id=cursor.getInt(cursor.getColumnIndex("_id"));

        cursor.close();
        mdbReader.close();
        mdbWriter.close();
        mySQLite.close();

        return id;

    }

    public void deleteAlarms(int position){
        mySQLite=new MySQLite(getApplicationContext());


        mdbWriter=mySQLite.getWritableDatabase();
        mdbReader=mySQLite.getReadableDatabase();

        cursor = mdbReader.query("Alarms",null,null,null,null,null,null);

        cursor.moveToPosition(position);
        int id=cursor.getInt(cursor.getColumnIndex("_id"));
        mdbWriter.delete("Alarms","_id=?",new String[]{id+""});



        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent();
        intent.setAction("com.example.yyj.remindyyj.ALARM");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,id,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);






        mDatas.remove(position);
        myAdapter.notifyItemRemoved(position);
        myAdapter.notifyItemChanged(0,mDatas.size()-2);

        cursor.close();
        mdbReader.close();
        mdbWriter.close();
        mySQLite.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
