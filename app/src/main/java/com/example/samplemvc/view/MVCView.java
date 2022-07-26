package com.example.samplemvc.view;

import android.view.View;

public interface MVCView {

    public View getRootView();
    public void initViews();
    public void bindDataToView();
}
