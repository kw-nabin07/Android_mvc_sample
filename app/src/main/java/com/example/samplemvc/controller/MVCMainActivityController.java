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

   public void onToDoItemSelected(long toDoId){
       mvcView.navigateToDataManipulationActivity(toDoId);
   }

}
