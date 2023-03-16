package com.example.samplemvc.view.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.MainActivity;
import com.example.samplemvc.R;
import com.example.samplemvc.model.bean.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewViewHolder> {

    private Context context;
    private List<ToDo> todos;
    ListItemClickListener listItemClickListener;

    public interface ListItemClickListener{
        void onItemClicked(long position);
    }

    public ToDoAdapter(Context context, List<ToDo> toDos, ListItemClickListener listItemClickListener){
        this.context = context;
        this.todos = toDos;
        this.listItemClickListener = listItemClickListener;
        if ( context instanceof MainActivity ) {
                todos = filterByToday();
        }

        this.todos.sort(new Comparator<ToDo>() {
            @Override
            public int compare(ToDo o1, ToDo o2) {
                Date date1 = null;
                Date date2 = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd h:mm a");
                try {
                    String dateTime1 = o1.getDate()+" "+o1.getTime();
                    String dateTime2 = o2.getDate()+" "+o2.getTime();
                    date1 = sdf.parse(dateTime1);
                    date2 = sdf.parse(dateTime2);
                }catch (Exception e){
                    System.out.println("Error parsing time strings: " + e.getMessage());
                }
                return date1.compareTo(date2);
            }
        });
    }
    public List<ToDo> filterByToday() {
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
            String today_date = year + "."+(month+1)+"."+day;
                if (itemDate.equals(today_date)) {
                    filteredList.add(item);
                }
        }
        return filteredList;
    }
    @Override
    public ToDoViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_row_item, parent, false);
        return new ToDoViewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ToDoViewViewHolder holder, final int position) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int AM = cal.get(Calendar.AM);
        String AM_PM;
        if(AM == 0){
            AM_PM = "AM";
        }else{
            AM_PM = "PM";
        }

        final ToDo toDo = todos.get(position);
        String todayDateTime = year + "." + (month+1) + "." + day + " " + hour + ":" + min + " "+ AM_PM;
        String itemDateTime = toDo.getDate() +" "+ toDo.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd h:mm a");
        Date dateToday = null;
        Date dateItem = null;
        try {
            // Parse the time strings into Date objects
            dateToday = sdf.parse(todayDateTime);
            dateItem = sdf.parse(itemDateTime);
        }catch (Exception e){
            System.out.println("Error parsing time strings: " + e.getMessage());
        }

        if(dateToday.compareTo(dateItem) > 0){
            holder.textViewDateTime.setText(toDo.getDate()+"   "+ toDo.getTime()+"　　 未完了");
            holder.textViewDateTime.setTextColor(Color.parseColor("#E83E31"));
        }else {
                holder.textViewDateTime.setText(toDo.getDate()+"  "+ toDo.getTime());
            }

        holder.textViewToDo.setText(toDo.getTitle());
        holder.textViewPlace.setText(toDo.getPlace());
        holder.layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listItemClickListener!=null){
                    listItemClickListener.onItemClicked(toDo.getId());
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.todo_row_item;
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class ToDoViewViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout layoutContainer;
        public TextView textViewToDo, textViewPlace,textViewDateTime;

        public ToDoViewViewHolder(View view){
            super(view);
            layoutContainer = (LinearLayout)view.findViewById(R.id.layoutContainer);
            textViewDateTime = (TextView)view.findViewById(R.id.DateTimeView);
            textViewToDo = (TextView)view.findViewById(R.id.textViewToDo);
            textViewPlace = (TextView)view.findViewById(R.id.textViewPlace);
        }


    }
}
