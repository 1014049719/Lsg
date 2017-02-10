package com.talenton.lsg.util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class UIHelper {

    public static void showDialog(AppCompatActivity activity, DialogFragment f, String tag) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        if (activity == null || activity.isFinishing()) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        f.show(ft, tag);
    }

    public static void showDialogAllowingStateLoss(AppCompatActivity activity, DialogFragment f, String tag){
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        if (activity == null || activity.isFinishing()) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment prev = fm.findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.add(f, null);
        //http://stackoverflow.com/questions/12105064/actions-in-onactivityresult-and-error-can-not-perform-this-action-after-onsavei
        ft.commitAllowingStateLoss();
    }
}
