package com.example.dormitory;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RefreshListView extends ListView implements OnScrollListener {

    String beforecolor;
    String aftercolor;
    String timecolor;
    String emptytext;
    String emptytextcolor;
    String emptyimagecolor;
    Drawable emptyimage;
    //释放刷新
    private final static int RELEASE_TO_REFRESH = 0;
    //下拉刷新
    private final static int PULL_TO_REFRESH = 1;
    //正在刷新
    private final static int REFRESHING = 2;
    //刷新完成
    private final static int DONE = 3;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RADIO = 3;

    private LayoutInflater mInflater;
    private LinearLayout mHeadView;
    private TextView mTipsTextView;
    private TextView mLastUpdatedTextView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;

    private RotateAnimation mAnimation;
    private RotateAnimation mReverseAnimation;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean mIsRecored;

    private int mHeadContentHeight;
    private int mStartY;
    private int mFirstItemIndex;
    private int mState;

    private boolean mISRefreshable;
    private OnRefreshListener mRefreshListener;

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 读取到传入的attrs
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RefreshListView);
        timecolor = array.getString(R.styleable.RefreshListView_TimeColor);
        beforecolor=array.getString(R.styleable.RefreshListView_TipColorBefore);
        aftercolor=array.getString(R.styleable.RefreshListView_TipColorAfter);
        emptytext=array.getString(R.styleable.RefreshListView_EmptyText);
        emptytextcolor=array.getString(R.styleable.RefreshListView_EmptyTextColor);
        emptyimage=array.getDrawable(R.styleable.RefreshListView_EmptyImage);
        emptyimagecolor=array.getString(R.styleable.RefreshListView_EmptyImageColor);
        array.recycle();

        init(context);
    }

    private void init(Context context) {

        mInflater = LayoutInflater.from(context);

        mHeadView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_header, null);

        mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
        mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
        mTipsTextView = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);

        mHeadContentHeight = mHeadView.getMeasuredHeight();
        System.out.println("mHeadContentHeight = " + mHeadContentHeight);
        mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);

        mHeadView.invalidate();

        addHeaderView(mHeadView, null, false);

        setOnScrollListener(this);

        mAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(250);
        mAnimation.setFillAfter(true);

        mReverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseAnimation.setInterpolator(new LinearInterpolator());
        mReverseAnimation.setDuration(250);
        mReverseAnimation.setFillAfter(true);

        mState = DONE;
        mISRefreshable = false;
    }

    private void measureView(View child) {
        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        System.out.println("params = " + params);
        if(params == null) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        System.out.println("lpWidth = " + params.width);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0+0, params.width);
        System.out.println("childWidthSpec = " + childWidthSpec);
        int lpHeight = params.height;
        System.out.println("lpHeight = " + lpHeight);
        int childHeightSpec;
        if(lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.UNSPECIFIED);
        }
        System.out.println("childHeightSpec = " + childHeightSpec);
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        mFirstItemIndex = firstVisibleItem;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    private void onRefresh() {
        if(mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    public void onRefreshComplete() {
        mArrowImageView.setImageResource(R.drawable.finish);
        mArrowImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(INVISIBLE);
        mTipsTextView.setText("刷新完成");
        mTipsTextView.setTextColor(getResources().getColor(R.color.black));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mState = DONE;
                mLastUpdatedTextView.setText("最近更新：" + new Date().toLocaleString());
                changeHeaderViewByState();
            }
        },1000);
    }

    public void setonRefreshListener(OnRefreshListener onRefreshListener) {
        this.mRefreshListener = onRefreshListener;
        mISRefreshable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mISRefreshable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(mFirstItemIndex == 0 && !mIsRecored) {
                        mIsRecored = true;
                        mStartY = (int) ev.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if(mState != REFRESHING) {
                        if(mState == DONE) {

                        }
                        if(mState == PULL_TO_REFRESH) {
                            mState = DONE;
                            changeHeaderViewByState();
                        }
                        if(mState == RELEASE_TO_REFRESH) {
                            mState = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }
                    mIsRecored = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) ev.getY();
                    if(!mIsRecored && mFirstItemIndex == 0) {
                        mIsRecored = true;
                        mStartY = tempY;
                    }
                    if(mState != REFRESHING && mIsRecored) {
                        if(mState == RELEASE_TO_REFRESH) {//释放状态：向上推，
                            setSelection(0);
                            if((tempY - mStartY)/RADIO < mHeadContentHeight && (tempY - mStartY) > 0) {
                                mState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            } else if(tempY - mStartY <= 0) {
                                mState = DONE;
                                changeHeaderViewByState();
                            }
                        }

                        if(mState == PULL_TO_REFRESH) {
                            setSelection(0);
                            if((tempY - mStartY)/RADIO >= mHeadContentHeight) {
                                mState = RELEASE_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                        } else if(tempY - mStartY <= 0) {
                            mState = DONE;
                            changeHeaderViewByState();
                        }
                        if(mState == DONE) {
                            if(tempY - mStartY > 0) {
                                mState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        if(mState == PULL_TO_REFRESH) {
                            mHeadView.setPadding(0, -1 * mHeadContentHeight + (tempY - mStartY)/RADIO, 0, 0);
                        }
                        if(mState == RELEASE_TO_REFRESH) {
                            mHeadView.setPadding(0, (tempY - mStartY)/RADIO - mHeadContentHeight, 0, 0);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    private void changeHeaderViewByState() {
        switch (mState) {
            case PULL_TO_REFRESH:
                mProgressBar.setVisibility(GONE);
                mTipsTextView.setVisibility(VISIBLE);
                mLastUpdatedTextView.setVisibility(VISIBLE);
                mArrowImageView.setVisibility(VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mReverseAnimation);
                mTipsTextView.setText("下拉可以刷新");
                break;

            case DONE:
                mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
                mProgressBar.setVisibility(GONE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setImageResource(R.drawable.arrow);
                mTipsTextView.setText("下拉可以刷新");
                mTipsTextView.setTextColor(getResources().getColor(R.color.black));
                mLastUpdatedTextView.setText("上次刷新时间:"+getCurrentTime());
                mLastUpdatedTextView.setVisibility(VISIBLE);
                break;

            case REFRESHING:
                mHeadView.setPadding(0, 0, 0, 0);
                mProgressBar.setVisibility(VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(GONE);
                mTipsTextView.setTextColor(getResources().getColor(R.color.blue));
                mTipsTextView.setText("正在刷新中");
                break;

            case RELEASE_TO_REFRESH:
                mArrowImageView.setVisibility(VISIBLE);
                mProgressBar.setVisibility(GONE);
                mTipsTextView.setVisibility(VISIBLE);
                mLastUpdatedTextView.setVisibility(VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mAnimation);
                mTipsTextView.setText("松开可以刷新");
                break;
            default:
                break;
        }
    }
    String getCurrentTime(){
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String CurrentTime = dff.format(new Date());
        return CurrentTime;
    }
}
