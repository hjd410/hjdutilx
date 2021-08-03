package com.hjd.apputils.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by HJD on 2019/12/10 and 10:20.
 */
public class LastInputEditText extends EditText {
    public LastInputEditText(Context context) {
        super(context);
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if (getText() == null) {//判空，防止出现空指针
            setSelection(0);
        } else {
            setSelection(getText().length()); // 保证光标始终在最后面
        }
    }
}
