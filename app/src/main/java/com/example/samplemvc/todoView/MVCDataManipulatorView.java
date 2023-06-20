package com.example.samplemvc.todoView;


public interface MVCDataManipulatorView extends MVCView {
    public void showSelectedToDo();
    public void updateViewOnRemove();
    public void updateViewOnModify();
    public void showErrorToast(String errorMessage);
}
