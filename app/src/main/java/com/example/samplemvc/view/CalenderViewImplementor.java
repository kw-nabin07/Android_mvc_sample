package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

public class CalenderViewImplementor implements MVCShowAllTodoActivityView,ToDoAdapter.ListItemClickListener{
    CalendarView calendarView;
    ToDoAdapter toDoAdapter;
    private static final String TAG = "CalenderViewImplementor";
    View rootView;
    MVCCalenderController mvcCalenderController;
    MVCModelImplementor mvcModel;
    private RecyclerView todoBySelected_date;
    int thisYear;
    int thisMonth;
    List<ToDo> allTodos;

    public CalenderViewImplementor(Context context, ViewGroup container) {
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_calender, container);
        mvcModel = new MVCModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcCalenderController = new MVCCalenderController(mvcModel, this);
    }
    @Override
    public void initViews() {
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        todoBySelected_date = (RecyclerView)rootView.findViewById(R.id.toDos_bySelected_date);
        todoBySelected_date.setLayoutManager(linearLayoutManager);
        //get selected date event
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                int year = clickedDayCalendar.get(Calendar.YEAR);
                int month = clickedDayCalendar.get(Calendar.MONTH);
                int day = clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
                String today_date = year + "-"+(month+1)+"-"+day;
                try {
                    filterBySelectedDate(today_date);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //get next month event
        Calendar cal1 = Calendar.getInstance();
        final int[] year = {cal1.get(Calendar.YEAR)};
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar cal = calendarView.getCurrentPageDate();
                int month = cal.get(Calendar.MONTH) + 1;
                if(month == 1){
                    year[0] = year[0] + 1;
                }
                thisYear = year[0];
                thisMonth = month;
                try {
                    filterByMonth(thisYear,thisMonth);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Log.d("eventDate",(Integer.toString(month)+","+Integer.toString(year[0])));
            }
        });
        //get previous month event
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar cal = calendarView.getCurrentPageDate();
                int month = cal.get(Calendar.MONTH) + 1;
                if(month == 12){
                    year[0] -= 1;
                }
                thisYear = year[0];
                thisMonth = month;
                try {
                    filterByMonth(thisYear,thisMonth);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Log.d("eventDate",(Integer.toString(month)+","+Integer.toString(year[0])));
            }
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
        toDoAdapter = new ToDoAdapter(rootView.getContext(),filterByToday(toDoList), this);
        todoBySelected_date.setAdapter(toDoAdapter);
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

        }else{
            Log.d("eventDate","EMPTY EVENT DATE");
        }
    }
    public List<ToDo> filterByToday(List<ToDo>todos) {
        // Get today's date
        Log.d("ToDoAdapter","filter method called.");
        // Filter the list by today's date
        List<ToDo> filteredList = new ArrayList<>();
        for (ToDo item : todos) {
            String itemDate = item.getDate();
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dateStr = null;
            String today_date = year + "-"+(month+1)+"-"+day;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date dateInput = sdf.parse(today_date);
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = sdfDate.format(dateInput);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (itemDate.equals(dateStr)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
    public void filterBySelectedDate(String selectedDate) throws Exception {
        // Get today's date
        Log.d("ToDoAdapter","filter method called.");
        // Filter the list by today's date
        List<ToDo> filteredList = new ArrayList<>();
        List<ToDo> allTodos = getAllData();
          for (ToDo item : allTodos) {
                String itemDate = item.getDate();
                String dateStr = null;
                String today_date = selectedDate;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date dateInput = sdf.parse(today_date);
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    dateStr = sdfDate.format(dateInput);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (itemDate.equals(dateStr)) {
                    filteredList.add(item);
                }
            }
            toDoAdapter = new ToDoAdapter(rootView.getContext(),filteredList, this);
            todoBySelected_date.setAdapter(toDoAdapter);
    }
    public void filterByMonth(int thisYear,int thisMonth) throws Exception {
        // Get today's date
        Log.d("ToDoAdapter","filter method called.");
        // Filter the list by today's date
        List<ToDo> filteredList = new ArrayList<>();
        List<ToDo> allTodos = getAllData();
        for (ToDo item : allTodos) {
            String itemDate = item.getDate();
            String[] monthYear = itemDate.split("-");
            if(Integer.parseInt(monthYear[0]) == thisYear){
                if(Integer.parseInt(monthYear[1]) == thisMonth){
                    filteredList.add(item);
                }
            }
        }
        toDoAdapter = new ToDoAdapter(rootView.getContext(),filteredList, this);
        todoBySelected_date.setAdapter(toDoAdapter);
    }
    List<ToDo> getAllData(){
        List<ToDo> allData = new ArrayList<>();
        try {
            allData = mvcModel.getAllToDos();
        } catch (Exception ex) {
            Toast.makeText(rootView.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return allData;
    }

}