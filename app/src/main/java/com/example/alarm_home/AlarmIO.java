package com.example.alarm_home;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AlarmIO {
    public  final String FILE_NAME = "data/data/com.example.alarm_home/files/alarm.txt";
    String lastOption;
    int numberOfAlarm, numberOfLastOption;
    ArrayList<String> alarmStringList;
    ArrayList<AddCheckBoxItem> addCheckBoxItemList;

    public AlarmIO(){
        alarmStringList = new ArrayList<>();
        addCheckBoxItemList = new ArrayList<>();
        numberOfAlarm = 0;
        numberOfLastOption = 0;
    }

    public void writeFile(){
        try{
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            String str;
            str = Integer.toString(numberOfAlarm);
            str += ':';
            str += getAddCheckBoxList();
            for(int i = 0; i < numberOfAlarm; i++){
                str += ';';
                str += alarmStringList.get(i);
            }
            outputStream.write(str.getBytes());
            outputStream.close();
        }
        catch ( Exception e){
            Log.w("FILE_WRITE", e.getMessage());
        }
    }

    public void readFile(){
        try{
            FileInputStream inputStream = new FileInputStream(FILE_NAME);
            byte[] tmp_byte = new byte[1000];
            inputStream.read(tmp_byte);
            String str = new String(tmp_byte).trim();
            inputStream.close();
            String[] main_splits = str.split(";", MainActivity.MAX + 1);
            lastOption = main_splits[0];
            setAddCheckBoxList(lastOption);

            int pos = 1;
            alarmStringList.clear();
            for(int i = 0; i < numberOfAlarm; i++){
                alarmStringList.add(main_splits[pos++]);
            }
        }
        catch (Exception e){
            Log.w("READ_FILE", e.getMessage());
        }
    }

    public String getAddCheckBoxList(){
        String str;
        str = Integer.toString(numberOfLastOption);
        for(int i = 0; i < numberOfLastOption; i++){
            str += ':';
            str += (addCheckBoxItemList.get(i).checked)? "1" : "0";
            str += ':';
            str += addCheckBoxItemList.get(i).context_title;
            str += ':';
            str += addCheckBoxItemList.get(i).context_time;
        }
        return str;
    }

    public void setAddCheckBoxList(String str){
        String[] sub_splits = str.split(":", MainActivity.MAX * 3 + 1);
        numberOfAlarm = Integer.parseInt(sub_splits[0]);
        numberOfLastOption = Integer.parseInt(sub_splits[1]);
        int pos = 2;
        for(int i = 0; i < numberOfLastOption; i++){
            AddCheckBoxItem item = new AddCheckBoxItem(
                    Integer.parseInt(sub_splits[pos++])== 1,
                    sub_splits[pos++],
                    sub_splits[pos++]
            );
            addCheckBoxItemList.add(item);
        }
    }

    public void setAlarmStringList(ArrayList<AlarmItem> alarmList){
        int size = alarmList.size();
        alarmStringList = new ArrayList<>();
        for(int i = 0; i < size; i++){
            alarmStringList.add(alarmList.get(i).getAlarmItemString());
        }
    }

    public void offAlarm(int index){
        readFile();
        for(int i = 0; i < numberOfAlarm; i++){
            AlarmItem alarmItem = new AlarmItem();
            alarmItem.setAlarmItemString(alarmStringList.get(i));
            if(alarmItem.index == index){
                alarmItem.isTurnOn = false;
                alarmStringList.set(i, alarmItem.getAlarmItemString());
            }
        }
        writeFile();
    }
}