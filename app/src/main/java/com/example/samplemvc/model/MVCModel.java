package com.example.samplemvc.model;


import com.example.samplemvc.model.bean.ToDo;

import java.util.List;

public interface MVCModel {
    public List<ToDo> getAllToDos() throws Exception;
    public ToDo getToDo(long id) throws Exception;
    public boolean addToDoItem(String toDoItem, String details, String date, String time,int notificationStatus) throws Exception;
    public boolean removeToDoItem(long id) throws Exception;
    public boolean modifyToDoItem(long id, String newToDoValue, String newDetailsValue,String newDateValue,String newTimeValue,int notificationStatus) throws Exception;
}
