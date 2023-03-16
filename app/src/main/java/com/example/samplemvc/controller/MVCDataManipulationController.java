package com.example.samplemvc.controller;


import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.view.DataManipulatorViewImplementor;

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

    public void onRemoveBottonClicked(long id){
       try{
           boolean success = mvcModel.removeToDoItem(id);
           if(success){
               mvcView.updateViewOnRemove();
           }
       }catch (Exception e){
           mvcView.showErrorToast(e.getMessage());
       }

   }

   public void onModifyButtonClicked(long id, String newTODo,String newAddress,String newDate, String newTime){
       try{
           boolean success = mvcModel.modifyToDoItem(id,newTODo,newAddress,newDate,newTime);
           if(success){
               mvcView.updateViewOnModify();
           }
       }catch (Exception e){
           mvcView.showErrorToast(e.getMessage());
       }
   }


}
