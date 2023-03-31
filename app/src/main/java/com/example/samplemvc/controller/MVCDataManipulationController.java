package com.example.samplemvc.controller;


import android.content.Context;

import com.example.samplemvc.NotificationHelper;
import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.view.DataManipulatorViewImplementor;

import java.util.List;

public class MVCDataManipulationController implements MVCController{

    MCVModelImplementor mvcModel;
    DataManipulatorViewImplementor mvcView;

    public MVCDataManipulationController(MCVModelImplementor mvcModel, DataManipulatorViewImplementor mvcView){
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
    }

    @Override
    public void onViewLoaded() {
        mvcView.showSelectedToDo();
    }

    public void onRemoveButtonClicked(Context context,long id){
       try{
           boolean success = mvcModel.removeToDoItem(id);
           if(success){
               List<ToDo> toDoList = mvcModel.getAllToDos();
               NotificationHelper.scheduleNotification(context, toDoList);
               mvcView.updateViewOnRemove();
           }
       }catch (Exception e){
           mvcView.showErrorToast(e.getMessage());
       }

   }

   public void onModifyButtonClicked(Context context,long id, String newTODo, String newDetails, String newDate, String newTime, int notificationStatus){
       try{
           boolean success = mvcModel.modifyToDoItem(id,newTODo,newDetails,newDate,newTime,notificationStatus);
           if(success){
               mvcView.updateViewOnModify();
               List<ToDo> toDoList1 = mvcModel.getAllToDos();
               NotificationHelper.scheduleNotification(context, toDoList1);

           }
       }catch (Exception e){
           mvcView.showErrorToast(e.getMessage());
       }
   }


}
