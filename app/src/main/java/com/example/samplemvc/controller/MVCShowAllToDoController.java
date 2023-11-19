package com.example.samplemvc.controller;

import com.example.samplemvc.model.MVCModelImplementor;
import com.example.samplemvc.todoView.AllToDoViewImplementor;

public class MVCShowAllToDoController implements MVCController {
    MVCModelImplementor mvcModel;
    AllToDoViewImplementor mvcView;

    public MVCShowAllToDoController(MVCModelImplementor mvcModel, AllToDoViewImplementor mvcView){
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
