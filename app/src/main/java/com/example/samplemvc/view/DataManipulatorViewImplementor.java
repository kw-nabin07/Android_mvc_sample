package com.example.samplemvc.view;

import static androidx.lifecycle.Transformations.map;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCDataManipulationController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DataManipulatorViewImplementor implements MVCDataManipulatorView {
    View rootView;

    MVCDataManipulationController mvcController;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private MCVModelImplementor mvcModel;

    EditText modifyTitle, modifyAddress, modifyDate, modifyTime;
    Button buttonRemoveToDo, buttonModifyToDo, buttonModifyDate, buttonModifyTime;

    ToDo toDo;

    long toDoId;

    public DataManipulatorViewImplementor(Context context, ViewGroup container, Intent intent) {
        rootView = LayoutInflater.from(context).inflate(R.layout.acivity_data_manipulate, container);
        mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        toDoId = intent.getLongExtra("todoId", 1);
        mvcController = new MVCDataManipulationController(mvcModel, this);
    }

    @Override
    public void initViews() {
        modifyTitle = (EditText) rootView.findViewById(R.id.modifyTitle);
        modifyAddress = (EditText) rootView.findViewById(R.id.modifyAddress);
        modifyDate = (EditText) rootView.findViewById(R.id.modifyDate);
        modifyTime = (EditText) rootView.findViewById(R.id.modifyTime);

        buttonRemoveToDo = (Button) rootView.findViewById(R.id.buttonRemoveToDo);
        buttonModifyToDo = (Button) rootView.findViewById(R.id.buttonModifyToDo);
        buttonModifyDate = (Button) rootView.findViewById(R.id.btnDatePickerForModify);
        buttonModifyTime = (Button) rootView.findViewById(R.id.btnTimePickerForModify);

        buttonModifyDate.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int style;
            @Override
            public void onClick(View v) {
                String[]oldDate = getDate();

               int yYear = Integer.parseInt(oldDate[0]);
               int mMonth =  Integer.parseInt(oldDate[1])-1;
               int dDay =  Integer.parseInt(oldDate[2]);
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "." + (month + 1) + "." + dayOfMonth;
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        modifyDate.setText(date);
                    }
                };
                style = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
                datePickerDialog = new DatePickerDialog(rootView.getContext(), style, dateSetListener, yYear, mMonth, dDay);
                //datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });

        buttonModifyTime.setOnClickListener(new View.OnClickListener() {
            int hours, minute;

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(rootView.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        hours = hour;
                        minute = min;
                        String time = hours + ":" + minute;
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = sdf.parse(time);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                            modifyTime.setText(sdf1.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();

                        }
                    }
                }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //timePickerDialog.updateTime(hours, minute);
                timePickerDialog.show();

            }
        });

        buttonRemoveToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcController.onRemoveBottonClicked(toDoId);
            }
        });

        buttonModifyToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = modifyTitle.getText().toString();
                String newAddress = modifyAddress.getText().toString();
                String newDateTxt = modifyDate.getText().toString();
                String newDate = newDateTxt.replace(" ","");
                String newTime = modifyTime.getText().toString();
                mvcController.onModifyButtonClicked(toDoId, newTitle, newAddress, newDate, newTime);
            }
        });


    }

    @Override
    public View getRootView() {
        return rootView;
    }

  String[] getDate() {
        String [] dateYear = new String[3];
           try {
            toDo = mvcModel.getToDo(toDoId);
            String date = toDo.getDate();
            String strDate = date.replace(".","/");
            dateYear = strDate.split("/");
            Log.d("Year","here4"+dateYear[0]+" "+dateYear[1]+" "+ dateYear[2]);

        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return dateYear;
    }


    @Override
    public void showSelectedToDo() {
        try{
            toDo = mvcModel.getToDo(toDoId);
            modifyTitle.setText(toDo.getTitle());
            modifyAddress.setText(toDo.getPlace());
            modifyDate.setText(toDo.getDate());
            modifyTime.setText(toDo.getTime());
            Log.d("selected function",toDo.getDate() +"id :" + toDoId);
        }catch (Exception ex){
            Toast.makeText(rootView.getContext(),ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void bindDataToView() {
        mvcController.onViewLoaded();
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnRemove() {
        modifyTitle.setText("");
        modifyAddress.setText("");
        modifyDate.setText("");
        modifyTime.setText("");
        Toast.makeText(rootView.getContext(),"Successfully removed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnModify() {
        Toast.makeText(rootView.getContext(),"Successfully updated", Toast.LENGTH_LONG).show();
    }


}
