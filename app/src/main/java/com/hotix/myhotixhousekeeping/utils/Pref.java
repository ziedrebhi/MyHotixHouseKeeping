package com.hotix.myhotixhousekeeping.utils;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

/**
 * Created by ziedrebhi_2 on 03/10/2016.
 */

public class Pref extends SwitchPreference {
    public Pref(Context context) {
        super(context);
    }

    public Pref(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Pref(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
