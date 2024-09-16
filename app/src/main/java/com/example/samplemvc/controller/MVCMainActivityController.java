package com.example.samplemvc.controller;

import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.todoView.MainActivityViewImplementor;


public class MVCMainActivityController implements MVCController{
    MVCModelImplementor mvcModel;
    MainActivityViewImplementor mvcView;

   public MVCMainActivityController(MVCModelImplementor mvcModel, MainActivityViewImplementor mvcView){
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
    }
    @Override
    public void onViewLoaded() {
        try{
            mvcView.showAllToDos(mvcModel.getAllToDos());
        }catch (Exception e){
            mvcView.showErrorToast(e.getMessage());
        }
    }
    public void onToDoItemSelected(long toDoId){
        mvcView.navigateToDataManipulationActivity(toDoId);
    }
}

