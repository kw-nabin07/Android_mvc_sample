package com.example.samplemvc.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.samplemvc.model.bean.ToDo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


public class ToDoListDBAdapter {

    private static final String TAG=ToDoListDBAdapter.class.getSimpleName();

    private static final String DB_NAME="todolist.db";
    private static final int DB_VERSION=2;

    private static final String TABLE_TODO="table_todo";
    private static final String COLUMN_TODO_ID="task_id";
    private static final String COLUMN_TODO="todo";
    private static final String COLUMN_INFO="details";
    private static final String COLUMN_DATE="date";
    private static final String COLUMN_TIME="time";
    private static final String COLUMN_NOTICE="notice_status";

    //create table table_todo(task_id integer primary key, todo text not null);
    private static String CREATE_TABLE_TODO="CREATE TABLE "+TABLE_TODO+"("+COLUMN_TODO_ID+" INTEGER PRIMARY KEY, "+COLUMN_TODO+" TEXT NOT NULL, "+
            COLUMN_INFO+ " TEXT NOT NULL,"+COLUMN_DATE+" TEXT NOT NULL,"+COLUMN_TIME+" TEXT NOT NULL,"+COLUMN_NOTICE+" INTEGER DEFAULT 0)";

    private Context context;
    private SQLiteDatabase  sqLliteDatabase;
    private static ToDoListDBAdapter toDoListDBAdapterInstance;


    private ToDoListDBAdapter(Context context){
        this.context=context;
        sqLliteDatabase=new ToDoListDBHelper(this.context,DB_NAME,null,DB_VERSION).getWritableDatabase();
    }

    public  static ToDoListDBAdapter getToDoListDBAdapterInstance(Context context){
        if(toDoListDBAdapterInstance==null){
            toDoListDBAdapterInstance=new ToDoListDBAdapter(context);
        }
        return toDoListDBAdapterInstance;
    }


    public boolean insert(String toDoItem, String details,String date, String time,int notificationStatus){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_TODO,toDoItem);
        contentValues.put(COLUMN_INFO,details);
        contentValues.put(COLUMN_DATE,date);
        contentValues.put(COLUMN_TIME,time);
        contentValues.put(COLUMN_NOTICE,notificationStatus);

        return sqLliteDatabase.insert(TABLE_TODO,null,contentValues)>0;
    }

    public boolean delete(long taskId){
       return sqLliteDatabase.delete(TABLE_TODO, COLUMN_TODO_ID+" = "+taskId,null)>0;
    }

    public boolean modify(long taskId, String newToDoItem, String newDetails, String newDate, String newTime,int newNotificationStatus){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_TODO,newToDoItem);
        contentValues.put(COLUMN_INFO,newDetails);
        contentValues.put(COLUMN_DATE,newDate);
        contentValues.put(COLUMN_TIME,newTime);
        contentValues.put(COLUMN_NOTICE,newNotificationStatus);

       return sqLliteDatabase.update(TABLE_TODO,contentValues, COLUMN_TODO_ID+" = "+taskId,null)>0;
    }

    public List<ToDo> getAllToDos(){
        List<ToDo> toDoList=new ArrayList<ToDo>();
        Cursor cursor=sqLliteDatabase.query(TABLE_TODO,new String[]{COLUMN_TODO_ID,COLUMN_TODO, COLUMN_INFO, COLUMN_DATE, COLUMN_TIME, COLUMN_NOTICE},null,null,null,null,null,null);
        if(cursor!=null &cursor.getCount()>0){
            while(cursor.moveToNext()){
                ToDo toDo=new ToDo(cursor.getLong(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5));
                toDoList.add(toDo);
            }
        }
        cursor.close();
        return toDoList;
    }
       private static class ToDoListDBHelper extends SQLiteOpenHelper{
        public ToDoListDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion){
            super(context,databaseName,factory,dbVersion);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
            Log.i(TAG,"Inside onConfigure");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_TODO);
            Log.i(TAG,"Inside onCreate");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                              int oldVersion, int newVersion) {
              switch (oldVersion){
                case 1: sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_TODO+ " ADD COLUMN "+COLUMN_INFO+" TEXT");break;
                default: break;
            }
            Log.i(TAG,"Inside onUpgrade");
        }
    }

}
