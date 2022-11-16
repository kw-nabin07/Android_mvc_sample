package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplemvc.DataManipulationActivity;
import com.example.samplemvc.MyApplication;
import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCMainActivityController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;
import com.example.samplemvc.view.adapters.ToDoAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivityViewImplementor implements MVCMainActivityView, ToDoAdapter.ListItemClickListener {

    View rootView;

    MVCMainActivityController mvcMainActivityController;

    private EditText editTextNewToDoString, editTextPlace;
    private RecyclerView recyclerView;
    private Button buttonAddToDo;

    ToDoAdapter toDoAdapter;

    public MainActivityViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_main,container);

        //MCVModelImplementor mvcModel = new MCVModelImplementor(MyApplication.getToDoListDBAdapter());
        MCVModelImplementor mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));

        mvcMainActivityController = new MVCMainActivityController(mvcModel, this);
    }


    @Override
    public void initViews() {
        editTextNewToDoString=(EditText)rootView.findViewById(R.id.editTextNewToDoString);
        editTextPlace=(EditText)rootView.findViewById(R.id.editTextPlace);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerListViewToDos);
        recyclerView.setLayoutManager(linearLayoutManager);

        buttonAddToDo=(Button)rootView.findViewById(R.id.buttonAddToDo);
        buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcMainActivityController.onAddButtonClicked(editTextNewToDoString.getText().toString(), editTextPlace.getText().toString());
            }
        });
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
    public void updateViewonAdd(List<ToDo> toDoList) {
        this.showAllToDos(toDoList);
        clearEditTexts();
    }

    @Override
    public void showAllToDos(List<ToDo> toDoList) {
        toDoAdapter = new ToDoAdapter(rootView.getContext(),toDoList, this);
        recyclerView.setAdapter(toDoAdapter);
    }

    private void clearEditTexts(){
        editTextNewToDoString.setText("");
        editTextPlace.setText("");
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
        recyclerView.setAdapter(toDoAdapter);
    }

    @Override
    public void onItemClicked(long position) {
        mvcMainActivityController.onToDoItemSelected(position);
    }

    @Override
    public  void navigateToDataManipulationActivity(long id){
        Intent intent = new Intent(rootView.getContext(), DataManipulationActivity.class);
        intent.putExtra("todoId", id);
        rootView.getContext().startActivity(intent);
    }
}
