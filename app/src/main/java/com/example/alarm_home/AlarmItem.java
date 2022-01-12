package com.example.alarm_home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmItem {
    int index;
    int curY, curM, curD, dayOfWeek;    // DatePickerDialog
    int hour_promise, minute_promise;
    int hour_basic, minute_basic;
    int basicTime;  // 약속시간에서 얼마나 뺄지 (- ~ 0)
    int missionTime;    // basictime에서 얼마나 더할지 ( 0 ~ 60)
    String title_promise;
    String missionFileName;
    boolean isTurnOn;
    public int numberOfCheckboxItem;
    ArrayList<AddCheckBoxItem> addCheckBoxList;

    public AlarmItem(){
        addCheckBoxList = new ArrayList<>();
    }

    public void setAlarmItemString(String newAlarm){
        String[] splits = newAlarm.split(":", MainActivity.ALARM_SIZE);
        Calendar calendar = Calendar.getInstance();
        index = Integer.parseInt(splits[0]);
        curY = Integer.parseInt(splits[1]);
        curM = Integer.parseInt(splits[2]);
        curD = Integer.parseInt(splits[3]);
        dayOfWeek = Integer.parseInt(splits[4]);
        hour_promise = Integer.parseInt(splits[5]);
        minute_promise =Integer.parseInt(splits[6]);
        isTurnOn = Integer.parseInt(splits[7]) == 1;
        basicTime = Integer.parseInt(splits[8]);
        calendar.set(Calendar.HOUR_OF_DAY, hour_promise);
        calendar.set(Calendar.MINUTE, minute_promise);
        calendar.add(Calendar.MINUTE, basicTime);
        hour_basic = calendar.get(Calendar.HOUR_OF_DAY);
        minute_basic = calendar.get(Calendar.MINUTE);
        missionTime = Integer.parseInt(splits[9]);
        title_promise = splits[10];
        missionFileName = splits[11];
        int pos = 12;
        numberOfCheckboxItem = Integer.parseInt(splits[pos]);
        for(int i = 0; i < numberOfCheckboxItem; i++){
            boolean checked = (Integer.parseInt(splits[++pos]) == 1) ? true : false;
            AddCheckBoxItem item = new AddCheckBoxItem(checked, splits[++pos], splits[++pos]);
            addCheckBoxList.add(item);
        }
    }

    public String getAlarmItemString(){
        numberOfCheckboxItem = addCheckBoxList.size();
        String str;
        str = Integer.toString(index);
        str += ':';
        str += Integer.toString(curY);
        str += ':';
        str += Integer.toString(curM);
        str += ':';
        str += Integer.toString(curD);
        str += ':';
        str += Integer.toString(dayOfWeek);
        str += ':';
        str += Integer.toString(hour_promise);
        str += ':';
        str += Integer.toString(minute_promise);
        str += ':';
        str += Integer.toString((isTurnOn)? 1 : 0);
        str += ':';
        str += Integer.toString(basicTime);
        str += ':';
        str += Integer.toString(missionTime);
        str += ':';
        str += title_promise;
        str += ':';
        str += missionFileName;
        str += ':';
        str += Integer.toString(numberOfCheckboxItem);
        for(int i = 0; i < numberOfCheckboxItem; i++){
            int tmp = (addCheckBoxList.get(i).checked) ? 1 : 0;
            str += ':';
            str += Integer.toString(tmp);
            str += ':';
            str += addCheckBoxList.get(i).context_title;
            str += ':';
            str += addCheckBoxList.get(i).context_time;
        }
        //str += '\n';  이거 하면 인텐트할 때 값을 못받음
        return str;
    }

    public String getDayOfWeekString(){
        switch (dayOfWeek){
            case 1: return "SUN";
            case 2: return "MON";
            case 3: return "TUE";
            case 4: return "WED";
            case 5: return "THUR";
            case 6: return "FRI";
            case 7: return "SAT";
        }
        return null;
    }

    public String getDate(){
        String tmp;
        tmp = Integer.toString(curY) + '.' + (curM + 1) + '.' + (curD) + '.';
        switch (dayOfWeek){
            case 1: tmp += "SUN";
                break;
            case 2: tmp += "MON";
                break;
            case 3: tmp += "TUE";
                break;
            case 4: tmp += "WED";
                break;
            case 5: tmp += "THUR";
                break;
            case 6: tmp += "FRI";
                break;
            case 7: tmp += "SAT";
                break;
        }
        return tmp;
    }

    public String getCheckBoxList(){
        String str;
        str = Integer.toString(numberOfCheckboxItem);
        for(int i = 0; i < numberOfCheckboxItem; i++){
            int tmp = (addCheckBoxList.get(i).checked) ? 1 : 0;
            str += ':';
            str += Integer.toString(tmp);
            str += ':';
            str += addCheckBoxList.get(i).context_title;
            str += ':';
            str += addCheckBoxList.get(i).context_time;
        }
        return str;
    }
}
