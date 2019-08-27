package com.hassanjamil.htwitter_sample_application.views;

import android.content.Context;
import android.util.AttributeSet;

import com.hassanjamil.htwitter_sample_application.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MyTwitterLoginButton extends TwitterLoginButton {
    public MyTwitterLoginButton(Context context) {
        super(context);
        init();
    }

    public MyTwitterLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable
                .ic_button_twitter), null, null, null);
        setBackgroundResource(android.R.color.transparent);
        setTextSize(12);
        setPadding(0, 0, 0, 0);
        setTextColor(getResources().getColor(R.color.tw__blue_default));
        //setTypeface(MyApplication.getInstance().getTypeface());
    }
}