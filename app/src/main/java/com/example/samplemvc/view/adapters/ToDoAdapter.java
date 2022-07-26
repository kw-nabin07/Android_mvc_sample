package com.example.samplemvc.view.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.R;
import com.example.samplemvc.model.bean.ToDo;

import java.util.List;


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
    }

    @Override
    public ToDoViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.todo_row_item, parent, false);

        return new ToDoViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDoViewViewHolder holder, final int position) {
        final ToDo toDo = todos.get(position);
        holder.textViewId.setText("Id: "+toDo.getId());
        holder.textViewToDo.setText("To Do: "+toDo.getToDo());
        holder.textViewPlace.setText("Place: "+toDo.getPlace());
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
        public TextView textViewId, textViewToDo, textViewPlace;

        public ToDoViewViewHolder(View view){
            super(view);
            layoutContainer = (LinearLayout)view.findViewById(R.id.layoutContainer);
            textViewId = (TextView)view.findViewById(R.id.textViewId);
            textViewToDo = (TextView)view.findViewById(R.id.textViewToDo);
            textViewPlace = (TextView)view.findViewById(R.id.textViewPlace);
        }


    }
}
