package com.example.samplemvc;

import static com.example.samplemvc.todoView.MVCViewFactory.getMVCView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samplemvc.todoView.MVCView;
import com.example.samplemvc.todoView.MVCViewFactory;

public class ShowAllToDoActivity extends AppCompatActivity {
    MVCView mvcView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvcView = getMVCView(MVCViewFactory.VIEW_TYPE.SHOW_TODO_VIEW_TYPE, ShowAllToDoActivity.this, null, null);
        setContentView(mvcView.getRootView());
        mvcView.initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvcView.bindDataToView();

    }
}