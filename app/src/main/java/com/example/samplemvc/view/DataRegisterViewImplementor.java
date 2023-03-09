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
import com.example.samplemvc.R;
import com.example.samplemvc.ShowAllToDoActivity;
import com.example.samplemvc.controller.MVCDataRegisterController;
import com.example.samplemvc.controller.MVCMainActivityController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;
import com.example.samplemvc.view.adapters.ToDoAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataRegisterViewImplementor implements MVCMainActivityView{

    View rootView;

    MVCDataRegisterController mvcDataRegisterController;
    private EditText editTextNewToDoString, editTextPlace;
    private Button buttonAddToDo;

    public DataRegisterViewImplementor (Context context, ViewGroup container){
        rootView = LayoutInflater.from(context).inflate(R.layout.activity_todo_register,container);
        MCVModelImplementor mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcDataRegisterController = new MVCDataRegisterController(mvcModel, this);
    }

    @Override
    public void initViews() {
        editTextNewToDoString=(EditText)rootView.findViewById(R.id.editTextNewToDoString);
        editTextPlace=(EditText)rootView.findViewById(R.id.editTextPlace);

        buttonAddToDo=(Button)rootView.findViewById(R.id.buttonAddToDo);

        buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcDataRegisterController.onAddButtonClicked(editTextNewToDoString.getText().toString(), editTextPlace.getText().toString());
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
    public void updateViewonAdd(List<ToDo> toDoList) {
        this.showAllToDos(toDoList);
        clearEditTexts();
    }

    @Override
    public void showAllToDos(List<ToDo> toDoList) {

    }

    private void clearEditTexts(){
        editTextNewToDoString.setText("");
        editTextPlace.setText("");
    }

    @Override
    public void showErrorToast(String errorMessage) {
        if(errorMessage.equals("Empty To Do List")){

        }
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }
    @Override
    public  void navigateToDataManipulationActivity(long id){

    }
}
