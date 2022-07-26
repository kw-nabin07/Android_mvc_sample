package com.example.samplemvc.model;


import com.example.samplemvc.exception.ToDoNotFoundException;
import com.example.samplemvc.model.bean.ToDo;
import com.example.samplemvc.model.db.ToDoListDBAdapter;

import java.util.List;

public class MCVModelImplementor implements MVCModel {

    ToDoListDBAdapter toDoListDBAdapter;

    List<ToDo> toDoItems;

    public MCVModelImplementor(ToDoListDBAdapter toDoListDBAdapter){
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
    public boolean addToDoItem(String toDoItem, String place) throws Exception{
        boolean addSuccess = toDoListDBAdapter.insert(toDoItem, place);
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
            throw new ToDoNotFoundException("Id is wrong");
        }
        return deleteSuccess;

    }

    @Override
    public boolean modifyToDoItem(long id, String newToDoValuel) throws Exception{
        boolean modifySuccess = toDoListDBAdapter.modify(id,newToDoValuel);
        if(modifySuccess){
            refresh();
        } else{
            throw new ToDoNotFoundException("Id is wrong");
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
            throw new ToDoNotFoundException("Id is wrong");
        }
        return toDo;
    }

    private void refresh(){
        toDoItems.clear();
        toDoItems = this.toDoListDBAdapter.getAllToDos();
    }


}
