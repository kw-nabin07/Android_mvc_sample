package com.example.samplemvc.view;

import com.example.samplemvc.model.bean.ToDo;

import java.util.List;

public interface MVCMainActivityView extends MVCView{
    public void showAllToDos(List<ToDo> toDoList);
    public void updateViewonAdd(List<ToDo> toDoList);
    public void showErrorToast(String errorMessage);
    public void navigateToDataManipulationActivity(long id);
}
