package com.example.samplemvc.todoView;

import android.view.View;

public interface MVCView {
    public View getRootView();
    public void initViews();
    public void bindDataToView();
}
