package com.example.dormitory.Student.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.dormitory.R;

public class DropDownAdapter extends PopupWindow {
    private Context mContext;
    private View view;


    public DropDownAdapter(final Context mContext) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.notepage_nomessage_layout, null);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);//高
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);//宽

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.AnimTop);
//  mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.type_text).getHeight();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //Y表示手指点击的位置，屏幕顶端为0，往下一次递增。height是popwin的高度。y > height就表示手指点在popwin的外面，然后关闭popwin
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }

        });
    }

}
