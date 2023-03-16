package com.example.samplemvc.view;

import com.example.samplemvc.model.bean.ToDo;

import java.util.List;


public interface MVCDataManipulatorView extends MVCView {
    public void showSelectedToDo();
    public void updateViewOnRemove();
    public void updateViewOnModify();
    public void showErrorToast(String errorMessage);
}
