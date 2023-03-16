package com.example.samplemvc.controller;

import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.view.MainActivityViewImplementor;


public class MVCMainActivityController implements MVCController{
    MCVModelImplementor mvcModel;
    MainActivityViewImplementor mvcView;

   public MVCMainActivityController(MCVModelImplementor mvcModel, MainActivityViewImplementor mvcView){
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
