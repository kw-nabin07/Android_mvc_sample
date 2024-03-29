package com.example.samplemvc.todoView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.DataManipulationActivity;
import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCShowAllToDoController;
import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;
import com.example.samplemvc.todoView.adapters.ToDoAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllToDoViewImplementor implements MVCShowAllTodoActivityView, ToDoAdapter.ListItemClickListener {
    View rootView;
    MVCShowAllToDoController mvcShowAllToDoController;
    private RecyclerView recyclerView_allToDos;
    ToDoAdapter toDoAdapter;

    public AllToDoViewImplementor(Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_show_all_to_do,container);
        MVCModelImplementor mvcModel = new MVCModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcShowAllToDoController = new MVCShowAllToDoController(mvcModel, this);
    }

    @Override
    public void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView_allToDos = (RecyclerView)rootView.findViewById(R.id.toDos_all);
        recyclerView_allToDos.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void bindDataToView() {
        mvcShowAllToDoController.onViewLoaded();
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void showAllToDos(List<ToDo> toDoList) {
        toDoAdapter = new ToDoAdapter(rootView.getContext(),toDoList, this);
        recyclerView_allToDos.setAdapter(toDoAdapter);
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
        recyclerView_allToDos.setAdapter(toDoAdapter);
    }
    @Override
    public  void navigateToDataManipulationActivity(long id){
        Intent intent = new Intent(rootView.getContext(), DataManipulationActivity.class);
        intent.putExtra("todoId", id);
        rootView.getContext().startActivity(intent);
    }
    @Override
    public void onItemClicked(long position) {
        mvcShowAllToDoController.onToDoItemSelected(position);
    }

}
