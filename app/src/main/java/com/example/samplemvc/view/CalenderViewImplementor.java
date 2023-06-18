package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.samplemvc.DataManipulationActivity;
import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCCalenderController;
import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;
import com.example.samplemvc.view.adapters.ToDoAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CalenderViewImplementor implements MVCShowAllTodoActivityView,ToDoAdapter.ListItemClickListener{
    CalendarView calendarView;
    ToDoAdapter toDoAdapter;
    private static final String TAG = "CalenderViewImplementor";
    View rootView;
    MVCCalenderController mvcCalenderController;
    private RecyclerView todoBySelected_date;
    List<ToDo> allTodos;
    public CalenderViewImplementor(Context context, ViewGroup container) {
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_calender, container);
        MVCModelImplementor mvcModel = new MVCModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcCalenderController = new MVCCalenderController(mvcModel, this);
    }
    @Override
    public void initViews() {
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        todoBySelected_date = (RecyclerView)rootView.findViewById(R.id.toDos_bySelected_date);
        todoBySelected_date.setLayoutManager(linearLayoutManager);
        //get selected date event
        calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR,clickedDayCalendar.get(Calendar.YEAR));
            selectedDate.set(Calendar.MONTH,clickedDayCalendar.get(Calendar.MONTH));
            selectedDate.set(Calendar.DATE,clickedDayCalendar.get(Calendar.DAY_OF_MONTH));
            filterBySelectedDate(selectedDate);
        });
        //get next month event
        Calendar cal1 = Calendar.getInstance();
        calendarView.setOnForwardPageChangeListener(() -> {
            Calendar cal = calendarView.getCurrentPageDate();
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal1.get(Calendar.YEAR);
            if(month == 1){
               year = year + 1;
            }
            filterByMonth(year,month);

        });
        //get previous month event
        calendarView.setOnPreviousPageChangeListener(() -> {
            Calendar cal = calendarView.getCurrentPageDate();
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal1.get(Calendar.YEAR);
            if(month == 12){
                year = year + 1;
            }
            filterByMonth(year,month);
        });

    }
    @Override
    public void bindDataToView() {
        mvcCalenderController.onViewLoaded();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void showAllToDos(List<ToDo> toDoList) {
        allTodos = toDoList;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        filterByMonth(year,month);
        //set Event
        setEventIcon(toDoList);
    }
    @Override
    public void showErrorToast(String errorMessage) {
        if(errorMessage.equals("Empty To Do List")){
            clearListView();
        }
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }
    private void clearListView(){
        toDoAdapter = new ToDoAdapter(rootView.getContext(), new ArrayList<>(), this);
        todoBySelected_date.setAdapter(toDoAdapter);
    }
    @Override
    public void navigateToDataManipulationActivity(long id) {
        Intent intent = new Intent(rootView.getContext(), DataManipulationActivity.class);
        intent.putExtra("todoId", id);
        rootView.getContext().startActivity(intent);
    }

    @Override
    public void onItemClicked(long position) {
        mvcCalenderController.onToDoItemSelected(position);
    }

    public void setEventIcon(List<ToDo> todoData){
        if(!todoData.isEmpty()){
            List<EventDay> events =  new ArrayList<>();
            Calendar[] cal = new Calendar[todoData.size()];
            for(int i = 0; i < todoData.size(); i++){
                String eventDate = todoData.get(i).getDate();
                String[] split_eventDate = eventDate.split("-");
                int year = Integer.parseInt(split_eventDate[0]);
                int month = Integer.parseInt(split_eventDate[1]) -1;
                int day = Integer.parseInt(split_eventDate[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.YEAR, year);
                cal[i] = calendar;
                events.add(new EventDay(cal[i], R.drawable.outline_event_available_24));
            }
            calendarView.setEvents(events);
        }
    }
    public void filterBySelectedDate(Calendar selectedDate) {
        List<ToDo> filteredList = new ArrayList<>();
        for (ToDo item : allTodos) {
            String itemDate = item.getDate();
            if(compareCalender(itemDate,selectedDate)){
                filteredList.add(item);
            }else {
                Log.d(TAG,"");
            }
         }
        setAdapter(filteredList);
    }
    public void filterByMonth(int thisYear,int thisMonth){
        List<ToDo> filteredList = new ArrayList<>();
        for (ToDo item : allTodos) {
            String itemDate = item.getDate();
            String[] monthYear = itemDate.split("-");
            if(Integer.parseInt(monthYear[0]) == thisYear){
                if(Integer.parseInt(monthYear[1]) == thisMonth){
                    filteredList.add(item);
                }
            }
        }
       setAdapter(filteredList);
    }
    boolean compareCalender(String itemDate,Calendar cal1){
        String[] date = itemDate.split("-");
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.YEAR,Integer.parseInt(date[0]));
        cal2.set(Calendar.MONTH,Integer.parseInt(date[1]) -1);
        cal2.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date[2]));
        if (Objects.equals(cal1, cal2)) {
            return true;
        }else {
          return false;
        }
    }
    public void setAdapter(List<ToDo>filteredList){
        toDoAdapter = new ToDoAdapter(rootView.getContext(),filteredList, this);
        todoBySelected_date.setAdapter(toDoAdapter);
    }

}