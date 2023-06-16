package com.example.samplemvc.controller;

import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.view.CalenderViewImplementor;

public class MVCCalenderController implements MVCController{

    MVCModelImplementor mvcModel;
    CalenderViewImplementor mvcView;

    public MVCCalenderController(MVCModelImplementor mvcModel, CalenderViewImplementor mvcView){
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
