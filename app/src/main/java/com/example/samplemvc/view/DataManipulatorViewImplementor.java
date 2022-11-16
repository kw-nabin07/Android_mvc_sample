package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samplemvc.MyApplication;
import com.example.samplemvc.R;
import com.example.samplemvc.controller.MVCDataManipulationController;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;


public class DataManipulatorViewImplementor implements MVCDataManipulatorView{


    View rootView;

    MVCDataManipulationController mvcController;

    private MCVModelImplementor mvcModel;

    TextView textViewToBeModifiedToDoId;
    EditText textViewToBeModifiedToDo, textViewToBeModifiedToDoPlace;
    Button buttonRemoveToDo, buttonModifyToDo;

    ToDo toDo;

    long toDoId;

    public DataManipulatorViewImplementor(Context context, ViewGroup container, Intent intent){
        rootView = LayoutInflater.from(context).inflate(R.layout.acivity_data_manipulate, container);
        //mvcModel = new MCVModelImplementor(MyApplication.getToDoListDBAdapter());
        mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        toDoId = intent.getLongExtra("todoId",1);
        //mvcModel = new MCVModelImplementor(ToDoListDBAdapter.getToDoListDBAdapterInstance(context));
        mvcController = new MVCDataManipulationController(mvcModel,this);
    }

    @Override
    public void initViews() {
        textViewToBeModifiedToDoId  = (TextView)rootView.findViewById(R.id.textViewToBeModifiedToDoId);
        textViewToBeModifiedToDo = (EditText)rootView.findViewById(R.id.textViewToBeModifiedToDo);
        textViewToBeModifiedToDoPlace = (EditText)rootView.findViewById(R.id.textViewToBeModifiedToDoPlace);

        buttonRemoveToDo = (Button)rootView.findViewById(R.id.buttonRemoveToDo);
        buttonModifyToDo = (Button)rootView.findViewById(R.id.buttonModifyToDo);


        buttonRemoveToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcController.onRemoveBottonClicked(toDoId);
            }
        });

        buttonModifyToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvcController.onModifyButtonClicked(toDoId,textViewToBeModifiedToDo.getText().toString(),textViewToBeModifiedToDoPlace.getText().toString());
            }
        });


    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void showSelectedToDo() {
        try{
            toDo = mvcModel.getToDo(toDoId);
            textViewToBeModifiedToDoId.setText("   "+ toDo.getId());
            textViewToBeModifiedToDo.setText(" "+toDo.getToDo());
            textViewToBeModifiedToDoPlace.setText(" "+toDo.getPlace());
        }catch (Exception ex){
            Toast.makeText(rootView.getContext(),ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void bindDataToView() {
        mvcController.onViewLoaded();
    }

    @Override
    public void showErrorToast(String errorMessage) {
        Toast.makeText(rootView.getContext(),errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnRemove() {
        textViewToBeModifiedToDoId.setText("");
        textViewToBeModifiedToDo.setText("");
        textViewToBeModifiedToDoPlace.setText("");
        Toast.makeText(rootView.getContext(),"Successfully removed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateViewOnModify(ToDo toDo) {
        this.toDo = toDo;
        textViewToBeModifiedToDo.setText(this.toDo.getToDo());
        Toast.makeText(rootView.getContext(),"Successfully updated", Toast.LENGTH_LONG).show();
    }


}
