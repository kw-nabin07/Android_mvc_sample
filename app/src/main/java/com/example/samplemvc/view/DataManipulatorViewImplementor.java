package com.example.samplemvc.view;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.samplemvc.controller.MVCDataManipulationController;
import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DataManipulatorViewImplementor implements MVCDataManipulatorView {
    public static final String TAG = "DataManipulatorViewImplementor";
    View rootView;

    MVCDataManipulationController mvcController;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private MVCModelImplementor mvcModel;

    EditText modifyTitle, modifyDetails, modifyDate, modifyTime;
    Button buttonRemoveToDo, buttonModifyToDo, buttonModifyDate, buttonModifyTime;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchModifyNotificationStatus;
    RadioGroup radioGroupNotifyTime;
    RadioButton radioButton_5min, radioButton_15min, radioButton_30min, radioButton_60min;
    int setNotificationShowTime;

    ToDo toDo;
    long toDoId;
    public DataManipulatorViewImplementor(Context context, ViewGroup container, Intent intent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.acivity_data_manipulate, container);
        mvcModel = new MVCModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        toDoId = intent.getLongExtra("todoId", 1);
        mvcController = new MVCDataManipulationController(mvcModel, this);
    }

    @Override
    public void initViews() {
        modifyTitle = (EditText) rootView.findViewById(R.id.modifyTitle);
        modifyDetails = (EditText) rootView.findViewById(R.id.modifyDetails);
        modifyDate = (EditText) rootView.findViewById(R.id.modifyDate);
        modifyTime = (EditText) rootView.findViewById(R.id.modifyTime);

        switchModifyNotificationStatus = (Switch) rootView.findViewById(R.id.notificationStatusChange);
        buttonRemoveToDo = (Button) rootView.findViewById(R.id.buttonRemoveToDo);
        buttonModifyToDo = (Button) rootView.findViewById(R.id.buttonModifyToDo);
        buttonModifyDate = (Button) rootView.findViewById(R.id.btnDatePickerForModify);
        buttonModifyTime = (Button) rootView.findViewById(R.id.btnTimePickerForModify);

        radioGroupNotifyTime = (RadioGroup) rootView.findViewById(R.id.radioGroupModify);
        radioButton_5min = (RadioButton) rootView.findViewById(R.id.radio_5m);
        radioButton_15min = (RadioButton) rootView.findViewById(R.id.radio_15m);
        radioButton_30min = (RadioButton) rootView.findViewById(R.id.radio_30m);
        radioButton_60min = (RadioButton) rootView.findViewById(R.id.radio_60m);

        buttonModifyDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance(Locale.JAPAN);
            int style;

            @Override
            public void onClick(View v) {
                String[] previousDate = getDate();

                int yYear = Integer.parseInt(previousDate[0]);
                int mMonth = Integer.parseInt(previousDate[1]) - 1;
                int dDay = Integer.parseInt(previousDate[2]);
                dateSetListener = (view, year, month, dayOfMonth) -> {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date dateInput = sdf.parse(date);
                        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                        String dateStr = sdfDate.format(dateInput);
                        modifyDate.setText(dateStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                style = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
                datePickerDialog = new DatePickerDialog(rootView.getContext(), style, dateSetListener, yYear, mMonth, dDay);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });

        buttonModifyTime.setOnClickListener(new View.OnClickListener() {
            final String[] previousTime = getTime();
            int hHour = Integer.parseInt(previousTime[0]);
            int mMinute = Integer.parseInt(previousTime[1]);

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(rootView.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        hHour = hour;
                        mMinute = min;
                        String time = hHour + ":" + mMinute;
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = sdf.parse(time);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("KK:mm aa");
                            modifyTime.setText(sdf1.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();

                        }

                    }
                }, hHour, mMinute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();

            }
        });
        int notice_status = getNotificationStatus();
        if (notice_status == 1) {
            switchModifyNotificationStatus.setChecked(true);
            radioGroupNotifyTime.setVisibility(View.VISIBLE);
        } else {
            radioGroupNotifyTime.setVisibility(View.INVISIBLE);
            switchModifyNotificationStatus.setChecked(false);
        }

        int noticeTime = getNotifyMinute();
        if(noticeTime != 0){
            switch (noticeTime) {
                case 5:
                    radioButton_5min.setChecked(true);
                    Log.d(TAG, " " + noticeTime);
                    break;
                case 15:
                    radioButton_15min.setChecked(true);
                    Log.d(TAG, " " + noticeTime);
                    break;
                case 30:
                    radioButton_30min.setChecked(true);
                    Log.d(TAG, " " + noticeTime);
                    break;
                case 60:
                    radioButton_60min.setChecked(true);
                    Log.d(TAG, " " + noticeTime);
                    break;
            }
        }

        switchModifyNotificationStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the switch is on, set the linear layout to be visible
                if (isChecked) {
                    radioGroupNotifyTime.setVisibility(View.VISIBLE);

                } else {
                    radioGroupNotifyTime.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioGroupNotifyTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                Toast.makeText(rootView.getContext(), setNotificationShowTime + " is selected", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRemoveToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcController.onRemoveButtonClicked(rootView.getContext(), toDoId);

            }
        });

        buttonModifyToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = modifyTitle.getText().toString();
                String newDetails = modifyDetails.getText().toString();
                String newDateTxt = modifyDate.getText().toString();
                String newDate = newDateTxt.replace(" ", "");
                String newTime = modifyTime.getText().toString();
                int noticeStatus = 0;
                if (switchModifyNotificationStatus.isChecked()) {
                    noticeStatus = 1;
                }
                mvcController.onModifyButtonClicked(rootView.getContext(), toDoId, newTitle, newDetails, newDate, newTime, noticeStatus, setNotificationShowTime);
            }
        });


    }

    @Override
    public View getRootView() {
        return rootView;
    }

    String[] getDate() {
        String[] dateYear = new String[3];
        try {
            toDo = mvcModel.getToDo(toDoId);
            String strDate = toDo.getDate();
            dateYear = strDate.split("-");
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return dateYear;
    }

    String[] getTime() {
        String[] itemTime = new String[2];
        String itemTimeF = null;
        try {
            toDo = mvcModel.getToDo(toDoId);
            String itemTimeStr = toDo.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            try {
                Date date = sdf.parse(itemTimeStr);
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                itemTimeF = sdf1.format(date);
            } catch (ParseException e) {
                e.printStackTrace();

            }
            itemTime = itemTimeF.split(":");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemTime;
    }

    public int getNotificationStatus() {
        int notificationStatus = 0;
        try {
            toDo = mvcModel.getToDo(toDoId);
            notificationStatus = toDo.getNotificationStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notificationStatus;
    }

    public int getNotifyMinute() {
        int notifyMinute = 0;
        try {
            toDo = mvcModel.getToDo(toDoId);
            notifyMinute = toDo.getNotificationMinute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifyMinute;
    }


    @Override
    public void showSelectedToDo() {
        try {
            toDo = mvcModel.getToDo(toDoId);
            modifyTitle.setText(toDo.getTitle());
            modifyDetails.setText(toDo.getDetails());
            modifyDate.setText(toDo.getDate());
            modifyTime.setText(toDo.getTime());
            Log.d(TAG + " selected Item", toDo.getId() + "," + toDo.getDate() + "," + toDo.getTime() + "," + toDo.getTitle() + "," + toDo.getNotificationStatus() + "," + toDo.getNotificationMinute());
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void bindDataToView() {
        mvcController.onViewLoaded();
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(rootView.getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnRemove() {
        modifyTitle.setText("");
        modifyDetails.setText("");
        modifyDate.setText("");
        modifyTime.setText("");
        switchModifyNotificationStatus.setChecked(false);
        Toast.makeText(rootView.getContext(), "Successfully removed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnModify() {
        Toast.makeText(rootView.getContext(), "Successfully updated", Toast.LENGTH_LONG).show();
    }

}
