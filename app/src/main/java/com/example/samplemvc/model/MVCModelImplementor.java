package com.example.samplemvc.model;


import com.example.samplemvc.exception.ToDoNotFoundException;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.util.List;

public class MVCModelImplementor implements MVCModel {
    ToDoListDBAdapter toDoListDBAdapter;

    List<ToDo> toDoItems;

    public MVCModelImplementor(ToDoListDBAdapter toDoListDBAdapter){
        this.toDoListDBAdapter = toDoListDBAdapter;
        toDoItems = this.toDoListDBAdapter.getAllToDos();
    }

    @Override
    public List<ToDo> getAllToDos() throws Exception{
        this.toDoItems = this.toDoListDBAdapter.getAllToDos();
        if(this.toDoItems!=null && this.toDoItems.size()>0){
            return this.toDoItems;
        } else {
          throw new Exception("Empty To Do List");
        }
    }

    @Override
    public boolean addToDoItem(String toDoItem, String details, String date, String time,int notificationStatus,int setNotifyMinute) throws Exception{
        boolean addSuccess = toDoListDBAdapter.insert(toDoItem, details, date, time, notificationStatus,setNotifyMinute);
        if (addSuccess){
            refresh();
        }else{
            throw new Exception("Some thing went wrong!!!");
        }

        return addSuccess;
    }

    @Override
    public boolean removeToDoItem(long id) throws Exception{
        boolean deleteSuccess = toDoListDBAdapter.delete(id);
        if(deleteSuccess){
            refresh();
        }else{
            throw new ToDoNotFoundException("Id is wrong in remove");
        }
        return deleteSuccess;

    }

    @Override
    public boolean modifyToDoItem(long id, String newToDoValue,String newDetailsValue,String newDateValue,String newTimeValue,int newNotificationStatus,int noticeMinute) throws Exception{
        boolean modifySuccess = toDoListDBAdapter.modify(id,newToDoValue,newDetailsValue,newDateValue,newTimeValue,newNotificationStatus,noticeMinute);
        if(modifySuccess){
            refresh();
        } else{
            throw new ToDoNotFoundException("Id is wrong in db");
        }
        return modifySuccess;
    }

    public ToDo getToDo(long id) throws Exception{
        ToDo toDo = null;
        for(ToDo toDo1: toDoItems){
            if(toDo1.getId()==id){
                toDo = toDo1;
                break;
            }
        }
        if(toDo==null){
            throw new ToDoNotFoundException("Id is wrong in get todo");
        }
        return toDo;
    }
    private void refresh(){
        toDoItems.clear();
        toDoItems = this.toDoListDBAdapter.getAllToDos();
    }

}
