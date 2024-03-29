package com.example.samplemvc.todoView.adapters;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.R;
import com.example.samplemvc.model.bean.ToDo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewViewHolder> {
    private static final String TAG = "ToDoAdapter";
    private final Context context;
    private final List<ToDo> todos;
    ListItemClickListener listItemClickListener;
    public interface ListItemClickListener{
        void onItemClicked(long position);
    }
    public ToDoAdapter(Context context, List<ToDo> toDos, ListItemClickListener listItemClickListener){
        Log.d(TAG,"called.");
        this.context = context;
        this.todos = toDos;
        this.listItemClickListener = listItemClickListener;
        this.todos.sort((o1, o2) -> {
            Date date1 = getDate(o1.getDate(), o1.getTime());
            Date date2 = getDate(o2.getDate(), o2.getTime());
            return date1.compareTo(date2);
        });
    }
    @NonNull
    @Override
    public ToDoViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_row_item,
                parent,
                false);
        return new ToDoViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewViewHolder holder, final int position) {
        Calendar cal = Calendar.getInstance();
        long todayDateTime = cal.getTimeInMillis();

        final ToDo toDo = todos.get(position);
        Date itemDate = getDate(toDo.getDate(), toDo.getTime());
        cal.setTime(itemDate);
        long itemDateTime = cal.getTimeInMillis();

        if(todayDateTime > itemDateTime){
            String dateAndTime = toDo.getDate()+"   "+ toDo.getTime()+"  未完了";
            holder.textViewDateTime.setText(dateAndTime);
            holder.textViewDateTime.setTextColor(Color.parseColor("#E83E31"));

        }else {
            String dateAndTime = toDo.getDate()+"  "+ toDo.getTime();
            holder.textViewDateTime.setText(dateAndTime);
            }
        holder.textViewToDo.setText(toDo.getTitle());
        holder.textViewDetails.setText(toDo.getDetails());
        holder.layoutContainer.setOnClickListener(v -> {
            if(listItemClickListener!=null){
                listItemClickListener.onItemClicked(toDo.getId());
            }
        });
    }
    public static Date getDate(String date, String time){
        Date formattedDate = null;
        String formattedTime = null;
        Locale locale = new Locale("jp");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",locale);
        try{
            Date dateTime = sdf.parse(time);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm",locale);
            if (dateTime != null) {
                formattedTime = sdf1.format(dateTime);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        String dateTime = date+" "+formattedTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm",locale);
        try {
            formattedDate = sdf1.parse(dateTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return formattedDate;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.todo_row_item;
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    static class ToDoViewViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout layoutContainer;
        public TextView textViewToDo, textViewDetails,textViewDateTime;

        public ToDoViewViewHolder(View view){
            super(view);
            layoutContainer = view.findViewById(R.id.layoutContainer);
            textViewDateTime = view.findViewById(R.id.DateTimeView);
            textViewToDo = view.findViewById(R.id.textViewToDo);
            textViewDetails = view.findViewById(R.id.textViewDetails);
        }
    }
}
