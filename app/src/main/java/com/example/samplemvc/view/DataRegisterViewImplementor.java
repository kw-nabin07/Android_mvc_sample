package com.example.samplemvc.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCDataRegisterController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DataRegisterViewImplementor implements MVCRegisterActivityView{

    View rootView;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    MVCDataRegisterController mvcDataRegisterController;
    private EditText text_todo, text_place,text_date,text_time;
    private Button buttonAddToDo,buttonDatePicker,buttonTimePicker;

    public DataRegisterViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_todo_register,container);
        MCVModelImplementor mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcDataRegisterController = new MVCDataRegisterController(mvcModel, this);
    }

    @Override
    public void initViews() {
        text_todo=(EditText)rootView.findViewById(R.id.editTextNewToDoString);
        text_place=(EditText)rootView.findViewById(R.id.editTextPlace);
        text_date=(EditText)rootView.findViewById(R.id.date_text);
        text_time=(EditText)rootView.findViewById(R.id.time_text);


        buttonAddToDo = (Button) rootView.findViewById(R.id.buttonAddToDo);
        buttonDatePicker = (Button) rootView.findViewById(R.id.btnDatePicker);
        buttonTimePicker = (Button) rootView.findViewById(R.id.btnTimePicker);

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            final Calendar cal = Calendar.getInstance();
            int yYear, mMonth, dDay,style;
            @Override
            public void onClick(View v) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "."+ (month + 1) + "." + dayOfMonth;
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH,month);
                        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        text_date.setText(date);
                    }
                };
                yYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                dDay = cal.get(Calendar.DAY_OF_MONTH);
                style = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
                datePickerDialog = new DatePickerDialog(rootView.getContext(),style,dateSetListener,yYear,mMonth,dDay);
                //datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }

        });

        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();
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
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try{
                            Date date = sdf.parse(time);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                            text_time.setText(sdf1.format(date));
                        }catch (ParseException e){
                            e.printStackTrace();

                        }
                    }
                },12,0,false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //timePickerDialog.updateTime(hHour,mMinute);
                timePickerDialog.show();

            }
        });
        buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = text_todo.getText().toString();
                String place = text_place.getText().toString();
                String strDate = text_date.getText().toString();
                String date = strDate.replace(" ","");
                String time = text_time.getText().toString();
                mvcDataRegisterController.onAddButtonClicked(todo, place, date, time);
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
        Toast.makeText(rootView.getContext(),"Successfully added.", Toast.LENGTH_LONG).show();
        clearEditTexts();
    }
    private void clearEditTexts(){
        text_todo.setText("");
        text_place.setText("");
        text_date.setText("");
        text_time.setText("");
    }

    @Override
    public void showErrorToast(String errorMessage) {
        if(errorMessage.equals("Empty To Do List")){

        }
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }

}
