package com.example.samplemvc;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samplemvc.todoView.MVCView;
import com.example.samplemvc.todoView.MVCViewFactory;


public class DataManipulationActivity extends AppCompatActivity {

    MVCView mvcView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvcView = MVCViewFactory.getMVCView(MVCViewFactory.VIEW_TYPE.MANIPULATION_VIEW_TYPE, DataManipulationActivity.this, null, getIntent());
        setContentView(mvcView.getRootView());
        mvcView.initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvcView.bindDataToView();
    }
}
