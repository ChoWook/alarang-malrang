package com.example.alarm_home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmItemAdapter extends BaseAdapter {
    private ArrayList<AlarmItem> alarmItemList = new ArrayList<>();
    AlarmIO alarmIO;
    public AlarmItemAdapter(){ alarmIO = new AlarmIO();}

    @Override
    public int getCount() {
        return alarmItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_alarm, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textAlarmTime = convertView.findViewById(R.id.txt_alarm_time);
        TextView textAlarmAmPm = convertView.findViewById(R.id.txt_alarm_am_pm);
        TextView textAlarmContext = convertView.findViewById(R.id.txt_alarm_context);
        Switch switchAlarm = convertView.findViewById(R.id.switch_alarm);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        boolean isAm = true;
        int hour = alarmItemList.get(pos).hour_basic;
        String str = "";
        if (hour > 12) {
            isAm = false;
            hour -= 12;
        }
        if (hour == 0) hour = 12;
        if (hour < 10) {
            str += '0';
        }
        str += Integer.toString(hour) + ':';
        if (alarmItemList.get(pos).minute_basic < 10) {
            str += '0';
        }
        str += Integer.toString(alarmItemList.get(pos).minute_basic);
        textAlarmTime.setText(str);
        textAlarmAmPm.setText(((isAm) ? "AM" : "PM"));
        hour = alarmItemList.get(pos).hour_promise;
        isAm = true;
        if (hour > 12) {
            isAm = false;
            hour -= 12;
        }
        if (hour == 0) hour = 12;
        str = Integer.toString(alarmItemList.get(pos).curY)
                + '.'
                + (alarmItemList.get(pos).curM + 1)
                + '.'
                + (alarmItemList.get(pos).curD)
                + '.'
                + alarmItemList.get(pos).getDayOfWeekString()
                + '.'
                + alarmItemList.get(pos).title_promise
                + " ";
        if (hour < 10) {
            str += '0';
        }
        str += (
                Integer.toString(hour)
                        + ':'
        );
        if (alarmItemList.get(pos).minute_promise < 10) {
            str += 0;
        }
        str += (
                (alarmItemList.get(pos).minute_promise)
                        + ((isAm) ? "AM" : "PM")
        );
        textAlarmContext.setText(str);

        switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                final Intent intent_basic = new Intent(context, AlarmReceiver.class);
                final Intent intent_mission = new Intent(context, AlarmReceiver.class);

                intent_basic.putExtra("receiver", alarmItemList.get(pos).getAlarmItemString());
                intent_mission.putExtra("receiver", alarmItemList.get(pos).getAlarmItemString());

                intent_basic.putExtra(MainActivity.ALARM_TYPE, 1);
                intent_mission.putExtra(MainActivity.ALARM_TYPE, 2);

                final PendingIntent operationBasic = PendingIntent.getBroadcast(context, alarmItemList.get(pos).index, intent_basic, PendingIntent.FLAG_UPDATE_CURRENT);
                final PendingIntent operationMission = PendingIntent.getBroadcast(context, alarmItemList.get(pos).index + 100, intent_mission, PendingIntent.FLAG_UPDATE_CURRENT);

                final AlarmManager alarmManagerBasic = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                final AlarmManager alarmManagerMission = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                if(checked){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, alarmItemList.get(pos).curY);
                    calendar.set(Calendar.MONTH, alarmItemList.get(pos).curM);
                    calendar.set(Calendar.DATE, alarmItemList.get(pos).curD);
                    calendar.set(Calendar.HOUR_OF_DAY, alarmItemList.get(pos).hour_basic);
                    calendar.set(Calendar.MINUTE, alarmItemList.get(pos).minute_basic);
                    calendar.set(Calendar.SECOND, 0);

                    Calendar curCalendar = Calendar.getInstance();

                    if(calendar.getTimeInMillis() > curCalendar.getTimeInMillis()){
                        if(alarmItemList.get(pos).missionTime != 0) {
                            calendar.set(Calendar.SECOND, 0);
                            alarmManagerBasic.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operationBasic);
                        }
                    }

                    calendar.add(Calendar.MINUTE, alarmItemList.get(pos).missionTime);

                    if(calendar.getTimeInMillis() > curCalendar.getTimeInMillis()) {
                        calendar.set(Calendar.SECOND, 0);
                        alarmManagerMission.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operationMission);
                    }
                }
                else{
                    alarmManagerBasic.cancel(operationBasic);
                    alarmManagerMission.cancel(operationMission);
                }
                alarmItemList.get(pos).isTurnOn = checked;
                notifyDataSetChanged();
            }
        });
        if(alarmItemList.get(pos).isTurnOn) {
            switchAlarm.setChecked(false);
            switchAlarm.setChecked(true);
        }
        else{
            switchAlarm.setChecked(false);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AlarmItem getItem(int position) { return alarmItemList.get(position); }

    public void addItem(String newAlarm){
        AlarmItem item = new AlarmItem();
        item.setAlarmItemString(newAlarm);

        for(int i = 0; i < alarmItemList.size(); i++){
            if(alarmItemList.get(i).index == item.index){
                alarmItemList.set(i, item);
                return;
            }
        }
        alarmItemList.add(item);
    }

    public void removeItem(int position){
        alarmItemList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        Log.w("SIZE", Integer.toString(alarmItemList.size()));
        alarmIO.numberOfAlarm = alarmItemList.size();
        alarmIO.setAlarmStringList(alarmItemList);
        alarmIO.writeFile();
    }
}
