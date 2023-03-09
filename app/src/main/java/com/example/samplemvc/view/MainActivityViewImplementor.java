package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.DataManipulationActivity;
import com.example.samplemvc.R;
import com.example.samplemvc.ShowAllToDoActivity;
import com.example.samplemvc.TodoRegisterActivity;
import com.example.samplemvc.controller.MVCMainActivityController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;
import com.example.samplemvc.view.adapters.ToDoAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivityViewImplementor implements MVCView{

    View rootView;
    MVCMainActivityController mvcMainActivityController;

    private EditText editTextNewToDoString, editTextPlace;


    public MainActivityViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_main,container);
        MCVModelImplementor mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcMainActivityController = new MVCMainActivityController(mvcModel, this);
    }

    @Override
    public void initViews() {
        TextView today_date = (TextView) rootView.findViewById(R.id.today_date);
        TextView time_now = (TextView) rootView.findViewById(R.id.time_now);
        TextView today_event = (TextView) rootView.findViewById(R.id.today_event);

        editTextNewToDoString=(EditText)rootView.findViewById(R.id.editTextNewToDoString);
        editTextPlace=(EditText)rootView.findViewById(R.id.editTextPlace);

        ImageButton buttonAddToDo = (ImageButton) rootView.findViewById(R.id.AddToDoBtn);
        ImageButton buttonViewToDo = (ImageButton) rootView.findViewById(R.id.EventListBtn);

        Thread thread = new Thread(new Runnable()
        {
            int lastMinute;
            int currentMinute;
            @Override
            public void run()
            {
                lastMinute = currentMinute;
                while (true)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    currentMinute = calendar.get(Calendar.MINUTE);
                    if (currentMinute != lastMinute){
                        lastMinute = currentMinute;
                        Locale jp = new Locale("ja", "JP", "JP");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E",jp);
                        today_date.setText(sdf.format(calendar.getTime()));
                        SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a",jp);
                        time_now.setText(sdf_time.format(calendar.getTime()));
                    }
                }
            }
        });
        thread.start();
        buttonViewToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), ShowAllToDoActivity.class);
                rootView.getContext().startActivity(intent);

            }
        });
        buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), TodoRegisterActivity.class);
                rootView.getContext().startActivity(intent);
               // mvcMainActivityController.onAddButtonClicked(editTextNewToDoString.getText().toString(), editTextPlace.getText().toString());
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

}
