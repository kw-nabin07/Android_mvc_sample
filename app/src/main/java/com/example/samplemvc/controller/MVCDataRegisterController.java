package com.example.samplemvc.controller;

import android.content.Context;

import com.example.samplemvc.NotificationHelper;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.view.DataRegisterViewImplementor;

import java.util.List;


public class MVCDataRegisterController{
    MCVModelImplementor mvcModel;
    DataRegisterViewImplementor mvcView;

    public MVCDataRegisterController(MCVModelImplementor mvcModel, DataRegisterViewImplementor mvcView){
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
    }
    public void onAddButtonClicked(Context context,String toDoItem, String details, String date, String time,int notificationStatus) {
        try{
            if(toDoItem.length() != 0 && details.length() != 0){
                boolean success = mvcModel.addToDoItem(toDoItem, details, date, time,notificationStatus);
                if(success){
                    mvcView.updateViewOnAdd();
                    if(notificationStatus == 1){
                        List<ToDo> toDoList = mvcModel.getAllToDos();
                        NotificationHelper.scheduleNotification(context, toDoList);
                    }
                }
            }else{
                throw new Exception("情報を入力してください。");
            }
        }catch (Exception e){
            mvcView.showErrorToast(e.getMessage());
        }
    }


}
