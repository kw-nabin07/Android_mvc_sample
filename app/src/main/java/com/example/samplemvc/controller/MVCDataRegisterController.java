package com.example.samplemvc.controller;

import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.view.DataRegisterViewImplementor;
import com.example.samplemvc.view.MainActivityViewImplementor;

public class MVCDataRegisterController{
    MCVModelImplementor mvcModel;
    DataRegisterViewImplementor mvcView;

    public MVCDataRegisterController(MCVModelImplementor mvcModel, DataRegisterViewImplementor mvcView){
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
    }
    public void onAddButtonClicked(String toDoItem, String place) {
        try{
            if(toDoItem.length() != 0 && place.length() != 0){
                boolean success = mvcModel.addToDoItem( toDoItem,  place);
                if(success){
                    mvcView.updateViewonAdd(mvcModel.getAllToDos());
                }
            }else {
                throw new Exception("情報を入力してください。");
            }

        }catch (Exception e){
            mvcView.showErrorToast(e.getMessage());
        }
    }

}
