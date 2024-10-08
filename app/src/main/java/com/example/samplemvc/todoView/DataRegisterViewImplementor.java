package com.example.samplemvc.todoView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCDataRegisterController;
import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DataRegisterViewImplementor implements MVCRegisterActivityView{
    private static final String TAG = "DataRegisterImplementor";

    View rootView;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    MVCDataRegisterController mvcDataRegisterController;
    private EditText text_todo, text_info,text_date,text_time;
    RadioGroup setMinuteRadioGroup;
    int setNotificationShowTime;

    public DataRegisterViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_todo_register,container);
        MVCModelImplementor mvcModel = new MVCModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcDataRegisterController = new MVCDataRegisterController(mvcModel, this);
    }

    @Override
    public void initViews() {
        text_todo=(EditText)rootView.findViewById(R.id.editTextNewToDoString);
        text_info=(EditText)rootView.findViewById(R.id.editTextDetails);
        text_date=(EditText)rootView.findViewById(R.id.date_text);
        text_time=(EditText)rootView.findViewById(R.id.time_text);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch notificationSwitch = (Switch) rootView.findViewById(R.id.notificationSwitch);

        Button buttonAddToDo = (Button) rootView.findViewById(R.id.buttonAddToDo);
        Button buttonDatePicker = (Button) rootView.findViewById(R.id.btnDatePicker);
        Button buttonTimePicker = (Button) rootView.findViewById(R.id.btnTimePicker);
        setMinuteRadioGroup = (RadioGroup) rootView.findViewById(R.id.setMinuteRadioGroup);

        Boolean notice_status = notificationSwitch.isChecked();

        setMinuteRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = rootView.findViewById(checkedId);
                String text = checkedRadioButton.getText().toString();
                switch (text) {
                    case "5分前":
                        setNotificationShowTime = 5;
                        break;
                    case "15分前":
                        setNotificationShowTime = 15;
                        break;
                    case "30分前":
                        setNotificationShowTime = 30;
                        break;
                    case "1時間前":
                        setNotificationShowTime = 60;
                        break;
                }

                //Toast.makeText(rootView.getContext(), setNotificationShowTime + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the switch is on, set the linear layout to be visible
                if (isChecked) {
                    setMinuteRadioGroup.setVisibility(View.VISIBLE);

                }else {

                    setMinuteRadioGroup.setVisibility(View.INVISIBLE);
                }
            }
        });
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int yYear, mMonth, dDay,style;
            @Override
            public void onClick(View v) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH,month);
                        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String date = year + "-"+ (month + 1) + "-" + dayOfMonth;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try{
                            Date dateInput = sdf.parse(date);
                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                            String dateStr = sdfDate.format(dateInput);
                            text_date.setText(dateStr);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                yYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                dDay = cal.get(Calendar.DAY_OF_MONTH);
                style = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
                datePickerDialog = new DatePickerDialog(rootView.getContext(),style,dateSetListener,yYear,mMonth,dDay);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });

        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();
            int hHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(rootView.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        hHour  = hour;
                        mMinute = min;
                        String time = hHour + ":" + mMinute;
                        Log.d(TAG,time);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try{
                            Date date = sdf.parse(time);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("KK:mm aa");
                            text_time.setText(sdf1.format(date));
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                },hHour,mMinute,true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = text_todo.getText().toString();
                String info = text_info.getText().toString();
                String strDate = text_date.getText().toString();
                String date = strDate.replace(" ","");
                String time = text_time.getText().toString();
                int notificationStatus;
                if (setNotificationShowTime == 0){
                    setNotificationShowTime = 15;
                }
                if(notice_status){
                    notificationStatus = 1;
                }else {
                    notificationStatus = 0;
                }
                mvcDataRegisterController.onAddButtonClicked(rootView.getContext(),todo, info, date, time,notificationStatus,setNotificationShowTime);
            }
        });
    }

    @Override
    public void bindDataToView() {

    }

    @Override
    public View getRootView() {
        return rootView;
    }


    @Override
    public void updateViewOnAdd() {
        Toast.makeText(rootView.getContext(),"成功しました。", Toast.LENGTH_LONG).show();
        clearEditTexts();
    }
    private void clearEditTexts(){
        text_todo.setText("");
        text_info.setText("");
        text_date.setText("");
        text_time.setText("");
    }

    @Override
    public void showErrorToast(String errorMessage) {
        if(errorMessage.equals("Empty To Do List")){
            Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
        }

    }

}
