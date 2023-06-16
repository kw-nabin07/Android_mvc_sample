package com.example.samplemvc.view;


public interface MVCDataManipulatorView extends MVCView {
    public void showSelectedToDo();
    public void updateViewOnRemove();
    public void updateViewOnModify();
    public void showErrorToast(String errorMessage);
}
