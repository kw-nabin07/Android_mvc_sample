package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivityViewImplementor implements MVCShowAllTodoActivityView,ToDoAdapter.ListItemClickListener{

    View rootView;
    MVCMainActivityController mvcMainActivityController;
    private RecyclerView today_event;

    ToDoAdapter toDoAdapter;


    public MainActivityViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_main,container);
        MCVModelImplementor mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcMainActivityController = new MVCMainActivityController(mvcModel, this);
    }

    @Override
    public void initViews() {
        TextView today_date = (TextView) rootView.findViewById(R.id.today_date);
        TextView time_now = (TextView) rootView.findViewById(R.id.time_now);
        TextView today_day = (TextView) rootView.findViewById(R.id.today_day);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        today_event = (RecyclerView) rootView.findViewById(R.id.recyclerListViewToDosToday);
        today_event.setLayoutManager(linearLayoutManager);

        ImageButton buttonAddToDo = (ImageButton) rootView.findViewById(R.id.AddToDoBtn);
        ImageButton buttonViewToDo = (ImageButton) rootView.findViewById(R.id.EventListBtn);
        Thread thread = new Thread(new Runnable()
        {
            int lastMinute;
            int currentMinute,currentDay;
            @Override
            public void run()
            {
                lastMinute = currentMinute;
                while (true)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    currentMinute = calendar.get(Calendar.MINUTE);
                    currentDay = calendar.get(Calendar.DAY_OF_WEEK);
                    if (currentMinute != lastMinute){
                        lastMinute = currentMinute;
                        Locale jp = new Locale("ja", "JP", "JP");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",jp);
                        today_date.setText(sdf.format(calendar.getTime()));
                        today_day.setText(getTodayDay(currentDay));
                        if(currentDay == 1 || currentDay == 7){
                            today_date.setTextColor(Color.parseColor("#E83E31"));
                            today_day.setTextColor(Color.parseColor("#E83E31"));
                        }
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
            }
        });

    }
    String getTodayDay(int day){
        String strToday = null;
        if(day == 1){
            strToday = "日曜日";
        }else if(day == 2){
            strToday = "月曜日";
        }else if(day == 3){
            strToday = "火曜日";
        }else if(day == 4){
            strToday = "水曜日";
        }else if(day == 5){
            strToday = "木曜日";
        }else if(day == 6){
            strToday = "金曜日";
        }else if(day == 7){
            strToday = "土曜日";
        }
        return strToday;
    }
    @Override
    public void bindDataToView() {
     mvcMainActivityController.onViewLoaded();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void showAllToDos(List<ToDo> toDoList) {
        toDoAdapter = new ToDoAdapter(rootView.getContext(),toDoList, this);
        today_event.setAdapter(toDoAdapter);
    }

    @Override
    public void showErrorToast(String errorMessage) {
        if(errorMessage.equals("Empty To Do List")){
            clearListView();
        }
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }
    private void clearListView(){
        toDoAdapter = new ToDoAdapter(rootView.getContext(), new ArrayList<ToDo>(), this);
        today_event.setAdapter(toDoAdapter);
    }

    @Override
    public void navigateToDataManipulationActivity(long id) {
        Intent intent = new Intent(rootView.getContext(), DataManipulationActivity.class);
        intent.putExtra("todoId", id);
        rootView.getContext().startActivity(intent);
    }

    @Override
    public void onItemClicked(long position) {
        mvcMainActivityController.onToDoItemSelected(position);
    }
}
