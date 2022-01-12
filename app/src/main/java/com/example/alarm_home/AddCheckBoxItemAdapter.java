package com.example.alarm_home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

public class AddCheckBoxItemAdapter extends BaseAdapter {
    private ArrayList<AddCheckBoxItem> addCheckBoxItemList = new ArrayList<>();

    public AddCheckBoxItemAdapter(){}

    @Override
    public int getCount() {
        return addCheckBoxItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        //int time;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_add_checkbox, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        CheckBox checkBox = convertView.findViewById(R.id.context_Check);
        final EditText editTextTitle = convertView.findViewById(R.id.context_Title);
        final EditText editTextTime = convertView.findViewById(R.id.context_Time);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        checkBox.setChecked(addCheckBoxItemList.get(pos).checked);
        editTextTitle.setText(addCheckBoxItemList.get(pos).context_title);
        editTextTime.setText(addCheckBoxItemList.get(pos).context_time);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                addCheckBoxItemList.get(pos).checked = checked;
            }
        });

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                addCheckBoxItemList.get(pos).context_title = editTextTitle.getText().toString();
            }
        });
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                addCheckBoxItemList.get(pos).context_time = editTextTime.getText().toString();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);

                dlg.setMessage("삭제하시겠습니까?");
                dlg.setNegativeButton("취소", null);
                dlg.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            removeItem(pos);
                        }
                        catch (Exception e){
                        }
                    }
                });
                dlg.show();
                return false;
            }
        });
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return addCheckBoxItemList.get(position);
    }

    public void addItem(boolean checked, String title, String time){
        AddCheckBoxItem item = new AddCheckBoxItem(checked, title, time);

        addCheckBoxItemList.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        addCheckBoxItemList.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<AddCheckBoxItem> getAddCheckBoxItemList(){
        return addCheckBoxItemList;
    }

    public void setAddCheckBoxItemList(ArrayList<AddCheckBoxItem> list){
        int size = list.size();
        addCheckBoxItemList = new ArrayList<>();
        for(int i = 0; i < size; i++){
            addItem(list.get(i).checked, list.get(i).context_title, list.get(i).context_time);
        }
    }

    public int getItemTime(int position){
        return Integer.parseInt(addCheckBoxItemList.get(position).context_time);
    }
}
