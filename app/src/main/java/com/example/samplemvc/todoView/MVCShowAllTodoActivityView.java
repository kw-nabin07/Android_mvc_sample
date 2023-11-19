package com.example.samplemvc.todoView;

import com.example.samplemvc.model.bean.ToDo;

import java.util.List;

public interface MVCShowAllTodoActivityView extends MVCView{
    public void showAllToDos(List<ToDo> toDoList);
    public void showErrorToast(String errorMessage);
    public void navigateToDataManipulationActivity(long id);
}
