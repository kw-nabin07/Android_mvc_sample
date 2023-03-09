package com.example.samplemvc.controller;

import com.example.samplemvc.model.MCVModelImplementor;
import com.example.samplemvc.view.MainActivityViewImplementor;


public class MVCMainActivityController{
    MCVModelImplementor mvcModel;
    MainActivityViewImplementor mvcView;

   public MVCMainActivityController(MCVModelImplementor mvcModel, MainActivityViewImplementor mvcView){
        this.mvcModel = mvcModel;
        this.mvcView = mvcView;
    }

}
