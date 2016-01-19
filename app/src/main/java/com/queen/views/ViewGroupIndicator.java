package com.queen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.queen.R;

import static android.graphics.Paint.Style;

/**
 * Created by liukun on 15/2/6.
 */
public class ViewGroupIndicator extends View implements IndicatorListener {

    public static final String TAG = ViewGroupIndicator.class.getSimpleName();

    private Context context;

    //绘制当前页面的Paint
    private Paint mSelectedPaint;
    //绘制其他页面的Paint
    private Paint mUnselectedPaint;

    //半径
    private int mSelectedRadius;
    private int mUnselectedRadius;

    //绘图颜色
    private int mSelectedColor;
    private int mUnselectedColor;
    //数量
    private int mIndicatorCount;
    //选中的下标
    private int mSelectedIndicatorIndex = 0;


    //两个点之间的距离
    private int dis;
    //当前页面指示器的x坐标
    private float indecatorX;

    private IndicatorParentImbl parent;

    //是否正在移动
    private boolean isMoving = false;
    //是否正在移动到下一个
    private boolean isMoveNext = false;

    //移动比率
    private float disR;

//    private boolean isFirstToLast = false;
//
//    private boolean isLastToFirst = false;

    public ViewGroupIndicator(Context context) {
        this(context, null);
    }

    public ViewGroupIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroupIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        initIndicator(attrs);

    }

    public void setParent(IndicatorParentImbl parent) {
        this.parent = parent;

        parent.setIndicatorListener(this);

        mIndicatorCount = parent.getIndicatorCount();
        invalidate();

    }


    //初始化
    private void initIndicator(AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.indicator,
                R.attr.indicatorDefStyleAttr,
                R.style.DefaultIndicatorTheme);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.indicator_radius:
                    mUnselectedRadius = mSelectedRadius = (int) a.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.indicator_distance_from_two_indicator:
                    dis = (int) a.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.indicator_selected_color:
                    mSelectedColor = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.indicator_unselected_color:
                    mUnselectedColor = a.getColor(attr, Color.WHITE);
                    break;

            }
        }
        a.recycle();

        mIndicatorCount = 0;


        mSelectedPaint = new Paint();
        mSelectedPaint.setColor(mSelectedColor);
        mSelectedPaint.setStrokeWidth(2);
        mSelectedPaint.setStyle(Style.FILL);
        mSelectedPaint.setAntiAlias(true);

        mUnselectedPaint = new Paint();
        mUnselectedPaint.setColor(mUnselectedColor);
        mUnselectedPaint.setStrokeWidth(2);
        mUnselectedPaint.setStyle(Style.STROKE);
        mUnselectedPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (mSelectedIndicatorIndex >= mIndicatorCount)
            mSelectedIndicatorIndex = mIndicatorCount - 1;
        if (mSelectedIndicatorIndex < 0)
            mSelectedIndicatorIndex = 0;

        int cx = (getWidth() - paddingLeft - paddingRight - mUnselectedRadius * 2 * (mIndicatorCount - 1) - dis * (mIndicatorCount - 1)) / 2;

        //获取到第一个点的x坐标
        float startX = (float) (getWidth() - paddingLeft - paddingRight - mUnselectedRadius * 2 * (mIndicatorCount - 1) - dis * (mIndicatorCount - 1)) / 2;

        int cy = getHeight() / 2;

        //绘制除当前页面的indicator之外的所有的点
        for (int i = 0; i < mIndicatorCount; i++) {
            canvas.drawCircle(cx, cy, mUnselectedRadius, mUnselectedPaint);
            cx += dis + mUnselectedRadius * 2;
        }

        if (indecatorX == 0) indecatorX = startX;

        //绘制指示当前页面的indicator
        canvas.drawCircle(indecatorX, cy, mSelectedRadius, mSelectedPaint);

//        Log.i("onDraw","indecatorX=" + indecatorX);

        //如果正在移动，根据移动比率进行移动
        if (isMoving) {

//            if ((mSelectedIndicatorIndex == mIndicatorCount - 1 && disR > 0) || (mSelectedIndicatorIndex == 0 && disR < 0)) {
//                indecatorX = startX + (dis + mUnselectedRadius * 2) * (mSelectedIndicatorIndex + disR);
//            }
            //    else {
            indecatorX = startX + (dis + mUnselectedRadius * 2) * (mSelectedIndicatorIndex + disR);
            //    }

//            Log.i("onDraw isMoving","(dis + mUnselectedRadius * 2) * disR=" + ((dis + mUnselectedRadius * 2) * disR));
            isMoving = false;
            invalidate();
        }


// else if (isMoveNext) {
//
//            //     Log.e(TAG, "isMoveNext");
//
//            //对从头到尾或从尾到头移动做了修改
//            if (isFirstToLast) {
//                indecatorX = startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex;
//                invalidate();
//                isMoveNext = false;
//                isFirstToLast = false;
//            } else if (isLastToFirst) {
//                indecatorX = startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex;
//                invalidate();
//                isMoveNext = false;
//                isLastToFirst = false;
//            }
//
//            //如果正在移动到下一个
//            if (indecatorX < (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex)) {
//                if ((indecatorX + 1) > (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex)) {
//                    indecatorX = (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex);
//                    isMoveNext = false;
//
//                } else {
//                    indecatorX++;
//                }
//                invalidate();
//            } else if (indecatorX > (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex)) {
//                if ((indecatorX - 1) < (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex)) {
//                    indecatorX = (startX + (dis + mUnselectedRadius * 2) * mSelectedIndicatorIndex);
//                    isMoveNext = false;
//                } else {
//                    indecatorX--;
//                }
//                invalidate();
//            }
//        }
    }


//    @Override
//    public void setNext(int index) {
//
//        isMoveNext = true;
//        isMoving = false;
//
//        if (index == mIndicatorCount - 1 && this.mSelectedIndicatorIndex == 0) {
//            //  Log.i("isFirstToLast", "isFirstToLast");
//            isFirstToLast = true;
//        } else if (index == 0 && this.mSelectedIndicatorIndex == mIndicatorCount - 1) {
//            // Log.i("isLastToFirst", "isLastToFirst");
//            isLastToFirst = true;
//        }
//        this.mSelectedIndicatorIndex = index;
//
//        //   Log.i("setNext", "mSelectedIndicatorIndex=" + mSelectedIndicatorIndex);
//
//        invalidate();
//    }

    @Override
    public void moving(int position, float dis) {
//        Log.i("moving", "position=" + position);
//        Log.i("moving", "dis=" + dis);

        isMoving = true;
        mSelectedIndicatorIndex = position;
        this.disR = dis;
        invalidate();

    }


//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        if (hasWindowFocus) {
//            if (parent != null) {
//                parent.onReDraw(mSelectedIndicatorIndex);
//                parent.startAutoScroll();
//            }
//        } else {
//            if (parent != null) {
//                parent.stopAutoScroll();
//            }
//        }
//    }

    public void start() {
        if (parent != null) {
            parent.onReDraw(mSelectedIndicatorIndex);
            parent.startAutoScroll();
        }
    }

    public void stop() {
        if (parent != null) {
            parent.stopAutoScroll();
        }
    }
    @Override
    public void notifyDateChanged(int count) {
        mIndicatorCount = count;
        invalidate();
    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//
//        SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//        Log.i("onRestoreInstanceState", "onRestoreInstanceState");
//
//        mSelectedIndicatorIndex = savedState.selectedIndicatorIndex;
//        indecatorX = savedState.indicatorX;
//
//        requestLayout();
//        if (parent != null) {
//            parent.onReDraw(mSelectedIndicatorIndex);
//            parent.startAutoScroll();
//        }
//
//    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        Log.i("onSaveInstanceState", "onSaveInstanceState");
//
//        savedState.selectedIndicatorIndex = mSelectedIndicatorIndex;
//        savedState.indicatorX = indecatorX;
//
//        Log.e("mSelectedIndicatorIndex", "mSelectedIndicatorIndex--> " + mSelectedIndicatorIndex);
//
//        if (parent != null) {
//            parent.stopAutoScroll();
//        }
//        return savedState;
//    }
//
//
//    static class SavedState extends BaseSavedState {
//        int selectedIndicatorIndex;
//        float indicatorX;
//
//        public SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        public SavedState(Parcel source) {
//            super(source);
//            selectedIndicatorIndex = source.readInt();
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest, flags);
//            dest.writeInt(selectedIndicatorIndex);
//            dest.writeFloat(indicatorX);
//        }
//
//        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel source) {
//                return new SavedState(source);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }
}
