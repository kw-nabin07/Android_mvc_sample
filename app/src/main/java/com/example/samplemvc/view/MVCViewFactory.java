package com.example.samplemvc.view;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

public class MVCViewFactory {

    public enum VIEW_TYPE{
        MAIN_VIEW_TYPE, MANIPULATION_VIEW_TYPE,SHOW_TODO_VIEW_TYPE,TODO_REGISTER_VIEW_TYPE
    }

    public static MVCView getMVCView(VIEW_TYPE viewType, Context context, ViewGroup viewGroup, Intent intent){
        MVCView mvcView = null;
        switch (viewType){
            case MAIN_VIEW_TYPE: mvcView = new MainActivityViewImplementor(context, viewGroup); break;
            case SHOW_TODO_VIEW_TYPE: mvcView = new AllToDoViewImplementor(context, viewGroup); break;
            case TODO_REGISTER_VIEW_TYPE: mvcView = new DataRegisterViewImplementor(context, viewGroup); break;
            case MANIPULATION_VIEW_TYPE:mvcView = new DataManipulatorViewImplementor(context, viewGroup, intent); break;

            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return mvcView;
    }
}
