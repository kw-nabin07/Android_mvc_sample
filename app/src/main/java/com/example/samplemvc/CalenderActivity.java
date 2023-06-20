package com.example.samplemvc;

import static com.example.samplemvc.todoView.MVCViewFactory.getMVCView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samplemvc.todoView.MVCView;
import com.example.samplemvc.todoView.MVCViewFactory;

public class CalenderActivity extends AppCompatActivity {
    MVCView mvcView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvcView = getMVCView(MVCViewFactory.VIEW_TYPE.CALENDER_VIEW_TYPE, CalenderActivity.this, null, null);
        setContentView(mvcView.getRootView());
        mvcView.initViews();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mvcView.bindDataToView();
    }

}