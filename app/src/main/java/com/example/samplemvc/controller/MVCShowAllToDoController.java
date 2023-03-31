package com.example.samplemvc.controller;

import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.view.AllToDoViewImplementor;

public class MVCShowAllToDoController implements MVCController {
    MCVModelImplementor mvcModel;
    AllToDoViewImplementor mvcView;

    public MVCShowAllToDoController(MCVModelImplementor mvcModel, AllToDoViewImplementor mvcView){
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
