package com.github.fujianlian.klinechart;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.VectorDrawable;
//import android.support.annotation.DimenRes;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GestureDetectorCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GestureDetectorCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.github.fujianlian.klinechart.base.IAdapter;
import com.github.fujianlian.klinechart.base.IChartDraw;
import com.github.fujianlian.klinechart.base.IDateTimeFormatter;
import com.github.fujianlian.klinechart.base.IValueFormatter;
import com.github.fujianlian.klinechart.draw.MainDraw;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.entity.IKLine;
import com.github.fujianlian.klinechart.entity.OrderEntity;
import com.github.fujianlian.klinechart.formatter.TimeFormatter;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;
import com.github.fujianlian.klinechart.utils.Constants;
import com.github.fujianlian.klinechart.utils.ViewUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.fujianlian.klinechart.utils.Constants.RANG_ITEM;

import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

/**
 * k??????
 * Created by tian on 2016/5/3.
 */
public abstract class BaseKLineChartView extends ScrollAndScaleView {
    /**
     * Order countdown flag
     */
    protected Bitmap mBitmapTimerIcon;
    private int mPointMin = 3;
    protected Bitmap mBitmapEllipseRed;
    protected Bitmap mBitmapEllipseGreen;
    protected Bitmap mBitmapVectorDown;
    protected Bitmap mBitmapVectorUp;
    public String duration;
    public String one;
    private String end;
    private String tradeStart;
    private int mChildDrawPosition = -1;

    private float mTranslateX = Float.MIN_VALUE;

    private int mWidth = 0;

    private int mTopPadding;

    private int mChildPadding;

    private int mBottomPadding;

    private float mMainScaleY = 1;

    private float mVolScaleY = 1;

    private float mChildScaleY = 1;

    private float mDataLen = 0;

    private float mMainMaxValue = Float.MAX_VALUE;

    private float mMainMinValue = Float.MIN_VALUE;

    private float mMainHighMaxValue = 0;

    private float mMainLowMinValue = 0;

    private int mMainMaxIndex = 0;

    private int mMainMinIndex = 0;

    private Float mVolMaxValue = Float.MAX_VALUE;

    private Float mVolMinValue = Float.MIN_VALUE;

    private Float mChildMaxValue = Float.MAX_VALUE;

    private Float mChildMinValue = Float.MIN_VALUE;

    private int mStartIndex = 0;

    private int mStopIndex = 0;

    private float mPointWidth = 6;

    private int mGridRows = 3;

    private int mGridColumns = 4;

    private int mDigit = 2;

    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint mTextPaintYellow = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSmallTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mMaxMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectedXLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectedYLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint m1MinuteAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint m1MinuteLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float mAnimatorValue = 0;

    private int mSelectedIndex;

    private IChartDraw mMainDraw;
    private MainDraw mainDraw;
    private IChartDraw mVolDraw;

    private IAdapter mAdapter;

    private Boolean isWR = false;
    private Boolean isShowChild = false;
    private Boolean isShowVOL = false;

    //TODO ??????????????????Value ??? 120
    private int defaultOverScrollRange = 120;
    public int getDefaultOverScrollRange() {
        return defaultOverScrollRange;
    }
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };
    //??????????????????
    private int mItemCount;
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();
    private List<IChartDraw> mVolDraws = new ArrayList<>();

    private IValueFormatter mValueFormatter;
    private IDateTimeFormatter mDateTimeFormatter;

    private ValueAnimator mAnimator;

    private long mAnimationDuration = 500;

    private float mOverScrollRange = 0;

    private OnSelectedChangedListener mOnSelectedChangedListener = null;

    private Rect mMainRect;

    private Rect mVolRect;

    private Rect mChildRect;

    private float mLineWidth;

//    private int mOrderIndex = -1;

//    private boolean isUp;//????????????

    private boolean isOption;//???????????????

    private int timeItem;//???????????????

    private int holdTimeItem;//???????????????????????????

    private String stopTime;//?????????????????????

    private String holdStopTime;//????????????????????????

    private String mStartCountTime;//??????????????????????????????

    private String newInfo;//??????????????????

    private int newPosition;//??????????????????

    //    private String newDate;//
    private Paint mEllipsePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private String period;

    private int mVolHeight;

    //    private Bitmap mBitmapUp, mBitmapDown;

    private Bitmap mBitmapStart, mBitmapStop;

    private Bitmap bitmapPoint;

    private float bottomY;//??????????????????
    private Paint mTextPaintRed = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<OrderEntity> orderEntities;//????????????????????????
    private List<OrderEntity> orderEntityList = new ArrayList<>();//????????????????????????????????? ??????????????????


    public BaseKLineChartView(Context context) {
        super(context);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = (int) getResources().getDimension(R.dimen.chart_top_padding);
        mChildPadding = (int) getResources().getDimension(R.dimen.child_top_padding);
        mBottomPadding = (int) getResources().getDimension(R.dimen.chart_bottom_padding);
        mBitmapEllipseRed = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ellipse);
        mBitmapVectorDown = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.vector);
        mBitmapEllipseGreen = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ellipse_green);
        mBitmapVectorUp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.vector_green);
        mEllipsePaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_white));
        mTextPaintRed.setColor(ContextCompat.getColor(getContext(), R.color.chart_red));
        mTextPaintRed.setTextSize(ViewUtil.Dp2Px(getContext(), 10));
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mTextPaintYellow.setColor(ContextCompat.getColor(getContext(), R.color.chart_yellow));
        mTextPaintYellow.setTextSize(ViewUtil.Dp2Px(getContext(), 10));
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mGreenPaint.setColor(Color.rgb(41,139,238));
        mWhitePaint.setColor(Color.parseColor("#ffffff"));
//        mBluePaint.setColor(ContextCompat.getColor(getContext(), R.color.color_6384FF));
        mBluePaint.setColor(Color.rgb(41,139,238));

        mTimePaint.setColor(Color.rgb(41,139,238));


        mWhitePaint.setTextSize(16);
        mTimePaint.setTextSize(16);
//        mSmallTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_white));
        mRedPaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_red));

        mSelectorFramePaint.setStrokeWidth(ViewUtil.Dp2Px(getContext(), 0.6f));
        mSelectorFramePaint.setStyle(Paint.Style.STROKE);
        mSelectorFramePaint.setColor(Color.WHITE);
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_up);
        bitmapPoint = BitmapFactory.decodeResource(getResources(), R.drawable.ic_point_kline);
        m1MinuteLinePaint.setColor(Color.parseColor("#ffffff"));
        m1MinuteLinePaint.setStrokeWidth(2);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        displayHeight = h - mTopPadding - mBottomPadding;
        initRect();
        setTranslateXFromScrollX(mScrollX);
    }

    int displayHeight = 0;

    private void initRect() {
        if (isShowChild) {
            int mMainHeight = (int) (displayHeight * 0.8f);
            mVolHeight = (int) (displayHeight * 0.0f);
            int mChildHeight = (int) (displayHeight * 0.2f);
            mMainRect = new Rect(0, mTopPadding, mWidth, mTopPadding + mMainHeight);
            mVolRect = new Rect(0, mMainRect.bottom + mChildPadding, mWidth, mMainRect.bottom + mVolHeight);
            mChildRect = new Rect(0, mVolRect.bottom + mChildPadding, mWidth, mVolRect.bottom + mChildHeight);
        } else if (isShowVOL) {
            int mMainHeight = (int) (displayHeight * 0.8f);
            mVolHeight = (int) (displayHeight * 0.2f);
            int mChildHeight = (int) (displayHeight * 0.0f);
            mMainRect = new Rect(0, mTopPadding, mWidth, mTopPadding + mMainHeight);
            mVolRect = new Rect(0, mMainRect.bottom + mChildPadding, mWidth, mMainRect.bottom + mVolHeight);
            mChildRect = new Rect(0, mVolRect.bottom + mChildPadding, mWidth, mVolRect.bottom + mChildHeight);
        } else {
            int mMainHeight = (int) (displayHeight * 1f);
            mVolHeight = (int) (displayHeight * 0.0f);
            mMainRect = new Rect(0, mTopPadding, mWidth, mTopPadding + mMainHeight);
            mVolRect = new Rect(0, mMainRect.bottom + mChildPadding, mWidth, mMainRect.bottom + mVolHeight);
        }

        mBitmapStart = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_start_time);
        mBitmapStop = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_stop_time);

        LinearGradient linearGradient = new LinearGradient(0, 0, 0,
                mMainRect.height(), ContextCompat.getColor(getContext(),
                R.color.one_minute_area_color), ContextCompat.getColor(getContext(), R.color.chart_line_background_color), Shader.TileMode.CLAMP);
        m1MinuteAreaPaint.setShader(linearGradient);
//        mBitmapUp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_up);
//        mBitmapDown = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_down_order);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundPaint.getColor());
        if (mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0) {
            return;
        }
        calculateValue();
        canvas.save();
        canvas.scale(1, 1);
        drawGird(canvas);
        drawK(canvas);
        drawText(canvas);
        draw1MinuteArea(canvas);

        IKLine point = (IKLine) getItem(mAdapter.getCount() - RANG_ITEM - 1);
        float y = getMainY(point.getClosePrice());
//        float x =  * mScaleX;

        renderBitmapEllipse(canvas,getChartWidth()/2,y-40,y,point.getClosePrice());
//        renderBitmapEllipse(canvas,180-lineEndRadius/2 * lineEndMaxMultiply - 80,getMainY(new BigDecimal(stringList.get(4)).floatValue())-10,getMainY(new BigDecimal(stringList.get(4)).floatValue())+mBitmapEllipseRed.getHeight()-15,new BigDecimal(stringList.get(4)).floatValue());
        renderBitmapEllipseGreen(canvas,getChartWidth()/2,y +40,y+15,point.getClosePrice());

//        drawValue(canvas, isLongPress ? mSelectedIndex : mStopIndex);
        drawCircle(canvas);
//        drawMaxAndMin(canvas);
        canvas.restore();


    }


    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public void setOption(boolean option) {
        isOption = option;
    }

    public int getTimeItem() {
        return timeItem;
    }

    public void setTimeItem(int timeItem) {
        this.timeItem = timeItem;
    }

    public void setHoldTimeItem(int holdTimeItem) {
        this.holdTimeItem = holdTimeItem;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public void setHoldStopTime(String holdStopTime) {
        this.holdStopTime = holdStopTime;
    }

    public void setmStartCountTime(String mStartCountTime) {
        this.mStartCountTime = mStartCountTime;
        invalidate();
    }

    public String getNewInfo() {
        return newInfo;
    }

    public int getNewPosition() {
        return newPosition;
    }

    public int getmPointMin() {
        return mPointMin;
    }
//    public String getNewDate() {
//        return newDate;
//    }

    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
//        this.newDate = date;
    }

    public void setNewInfo(String newInfo) {
        this.newInfo = newInfo;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public float getMainY(float value) {
        return (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
    }

    public float getMainBottom() {
        return mMainRect.bottom;
    }

    public float getVolY(float value) {
        return (mVolMaxValue - value) * mVolScaleY + mVolRect.top;
    }

    public float getChildY(float value) {
        return (mChildMaxValue - value) * mChildScaleY + mChildRect.top;
    }

    /**
     * ??????text???????????????
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return y + fontMetrics.descent - fontMetrics.ascent;
    }

    /**
     * ??????text???????????????
     */
    public float fixTextY1(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * ?????????
     *
     * @param canvas
     */
    private void drawGird(Canvas canvas) {
        //-----------------------??????k??????------------------------
        //?????????grid
        float rowSpace = mMainRect.height() / mGridRows;
        int mSmallWidth = 20;//????????????
        int interval = 20;
        int number = mWidth / mSmallWidth;
        for (int i = 0; i <= mGridRows; i++) {
            for (int i1 = 0; i1 < number; i1++) {
                if ((i1 + 1) * mSmallWidth + interval * i1 < mWidth) {
                    canvas.drawLine(i1 * mSmallWidth + interval * i1, rowSpace * i + mMainRect.top, (i1 + 1) * mSmallWidth + interval * i1, rowSpace * i + mMainRect.top, mGridPaint);
                }
            }
        }
        //-----------------------????????????------------------------
        if (mChildDraw != null || mVolDraw != null) {
            for (int i1 = 0; i1 < number; i1++) {
                if ((i1 + 1) * mSmallWidth + interval * i1 < mWidth) {
                    canvas.drawLine(i1 * mSmallWidth + interval * i1, mVolRect.bottom, (i1 + 1) * mSmallWidth + interval * i1, mVolRect.bottom, mGridPaint);
                    canvas.drawLine(i1 * mSmallWidth + interval * i1, mChildRect.bottom, (i1 + 1) * mSmallWidth + interval * i1, mChildRect.bottom, mGridPaint);
                }
            }
        }
        //?????????grid
        float columnSpace = mWidth / mGridColumns;
        for (int i = 1; i < mGridColumns; i++) {
            for (int i1 = 0; i1 < number; i1++) {
                if ((i1 + 1) * mSmallWidth + interval * i1 < mMainRect.bottom) {
                    canvas.drawLine(columnSpace * i, i1 * mSmallWidth + interval * i1, columnSpace * i, (i1 + 1) * mSmallWidth + interval * i1, mGridPaint);

                }
            }
            if (mChildDraw != null || mVolDraw != null) {
                for (int i1 = 0; i1 < number; i1++) {
                    if ((i1 + 1) * mSmallWidth + interval * i1 < mChildRect.bottom || (i1 + 1) * mSmallWidth + interval * i1 < mVolRect.bottom) {
                        canvas.drawLine(columnSpace * i, mVolRect.bottom - mVolHeight + i1 * mSmallWidth + interval * i1, columnSpace * i, mVolRect.bottom - mVolHeight + (i1 + 1) * mSmallWidth + interval * i1, mGridPaint);

                    }
                }
            }
        }
    }

    /**
     * ???????????????1????????????
     *
     * @param canvas
     */
    private void draw1MinuteArea(Canvas canvas) {
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        //TODO ?????????????????????  Log.d("1112220009", " draw1MinuteArea mPointWidth:" + mPointWidth + " startX:" + startX + "  te:" + te);
        float startX1 = 0.0f,endX1 = 0.0f;
//        float startX = mMainRect.right - mPointWidth * defaultOverScrollRange * mScaleX;
//        float te = mMainRect.width() / mPointWidth;
//        Log.d("1112220009", " draw1MinuteArea mPointWidth:" + mPointWidth + " startX:" + startX + "  te:" + te);
        float startX = 0f,endX = 0f;
        startX1 = calculateDraw1MinuteAreaPosition();
        endX1 = calculateDraw1MinuteAreaEndXPosition();
        //TODO 1????????????
        canvas.drawRect(startX1 - 360, 0, endX1 - 360, mMainRect.bottom, m1MinuteAreaPaint);
        //TODO 1?????????????????????
//        canvas.drawLine(startX1 - 360, 0, startX1- 360, mMainRect.bottom, m1MinuteLinePaint);
        //TODO 1?????????????????????
        canvas.drawLine(endX1 - 360, 0, endX1, mMainRect.bottom, m1MinuteLinePaint);
        canvas.restore();
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(1, 1);
        startX = startX1 * mScaleX;
        endX = endX1 * mScaleX;
        duration = "Duration";
        float textWidthDuration = mTextPaintRed.measureText(duration);
        Paint.FontMetrics fm = mTextPaintRed.getFontMetrics();
        float textHeightDuration = fm.descent - fm.ascent;
        canvas.drawText(duration, startX + (endX - startX) / 2 - textWidthDuration / 2 - 360, mMainRect.top + 20, mTextPaintRed);

        one = "1min";
        float textWidthOne = mTextPaint.measureText(one);
        canvas.drawText(one, startX + (endX - startX) / 2 - textWidthOne / 2 - 360, mMainRect.top + textHeightDuration + 30, mTextPaint);

//        tradeStart = "Trade start";
//        float textWidthTradeStart = mTextPaintYellow.measureText(tradeStart);
//        canvas.drawText(tradeStart, startX - textWidthTradeStart - 15 - 360, mMainRect.top + 20, mTextPaintYellow);

        end = "End of";
        canvas.drawText(end, endX + 15, mMainRect.top + 20, mTextPaintYellow);
        canvas.drawText("", endX + 15, mMainRect.top + 50, mTextPaintYellow);
        canvas.restore();
//        if (getmPointMin() != 60 && getmPointMin() != 30){
//            duration = "Duration";
//            float textWidthDuration = mTextPaintRed.measureText(duration);
//            Paint.FontMetrics fm = mTextPaintRed.getFontMetrics();
//            float textHeightDuration = fm.descent - fm.ascent;
//            canvas.drawText(duration, startX + (endX - startX) / 2 - textWidthDuration / 2, mMainRect.top + 20, mTextPaintRed);
//
//            one = "1min";
//            float textWidthOne = mTextPaint.measureText(one);
//            canvas.drawText(one, startX + (endX - startX) / 2 - textWidthOne / 2, mMainRect.top + textHeightDuration + 30, mTextPaint);
//
//            tradeStart = "Trade start";
//            float textWidthTradeStart = mTextPaintYellow.measureText(tradeStart);
//            canvas.drawText(tradeStart, startX - textWidthTradeStart - 15, mMainRect.top + 20, mTextPaintYellow);
//
//            end = "End of";
//            canvas.drawText(end, endX + 15, mMainRect.top + 20, mTextPaintYellow);
//            canvas.drawText("", endX + 15, mMainRect.top + 50, mTextPaintYellow);
//            canvas.restore();
//        } else {
//            duration = "";
//            float textWidthDuration = mTextPaintRed.measureText(duration);
//            Paint.FontMetrics fm = mTextPaintRed.getFontMetrics();
//            float textHeightDuration = fm.descent - fm.ascent;
//            canvas.drawText(duration, startX + (endX - startX) / 2 - textWidthDuration / 2, mMainRect.top + 20, mTextPaintRed);
//
//            one = "";
//            float textWidthOne = mTextPaint.measureText(one);
//            canvas.drawText(one, startX + (endX - startX) / 2 - textWidthOne / 2, mMainRect.top + textHeightDuration + 30, mTextPaint);
//
//            tradeStart = "";
//            float textWidthTradeStart = mTextPaintYellow.measureText(tradeStart);
//            canvas.drawText(tradeStart, startX - textWidthTradeStart - 15, mMainRect.top + 20, mTextPaintYellow);
//
//            end = "";
//            canvas.drawText(end, endX + 15, mMainRect.top + 20, mTextPaintYellow);
//            canvas.drawText("", endX + 15, mMainRect.top + 50, mTextPaintYellow);
//            canvas.restore();
//        }

    }
    //draw1MinuteArea
    private float calculateDraw1MinuteAreaPosition(){
        float startX1 = 0.0f;
        if (getmPointMin() == 3){
            startX1 = getX(mItemCount - 2) + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 15){
            startX1 = getX(mItemCount - 2 + defaultOverScrollRange / 2)  + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 30){
            startX1 = getX(mItemCount - 2 + defaultOverScrollRange * 3 / 4) + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 60){
            startX1 = getX(mItemCount - 6 + defaultOverScrollRange * 2 / 2) + (float) mAnimatorValue * mPointWidth;
        }
        return startX1;
    }
    private float calculateDraw1MinuteAreaEndXPosition(){
        float endX1 = 0.0f;
        if (getmPointMin() == 3){
            endX1 = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 15){
            endX1 = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 30){
            endX1 = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        }else if (getmPointMin() == 60){
            endX1 = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        }
        return endX1;
    }
    public final float calculateStartXOriginSpotPosition() {
        float startX = 0.0f;
//        startX = getX(mItemCount - 2) + (float) mAnimatorValue * mPointWidth;
        if (getmPointMin() == 3) {
            startX = getX(mItemCount - 2) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 15) {
            startX = getX(mItemCount - 2 + defaultOverScrollRange / 2) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 30) {
            startX = getX(mItemCount - 2 + defaultOverScrollRange / 2 - 40 * 3 / 4) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 60) {
            startX = getX(mItemCount - 2 * 2 / 2) + (float) mAnimatorValue * mPointWidth;
        }
//        timeStampStopX = calculateStartXOriginSpotPosition();
        return startX;
    }

    public final float calculateEndXOriginSpotPosition() {
        float endX = 0.0f;
        if (getmPointMin() == 3) {
            endX = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 15) {
            endX = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 30) {
            endX = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        } else if (getmPointMin() == 60) {
            endX = getX(mItemCount - 2 + defaultOverScrollRange) + (float) mAnimatorValue * mPointWidth;
        }
        return endX;
    }


    /**
     * ???k??????
     *
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //??????????????????????????????
        canvas.save();
        if (isFullScreen()) {
            canvas.translate(mTranslateX * mScaleX, 0);
        } else {
            canvas.translate(0, 0);
        }
        canvas.scale(mScaleX, 1);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            float currentPointX = getX(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw != null) {
                if (i < mItemCount - (RANG_ITEM))
                    mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mVolDraw != null) {
                if (i < mItemCount - (RANG_ITEM))
                    mVolDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mChildDraw != null) {
                if (i < mItemCount - (RANG_ITEM))
                    mChildDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }

        //????????????????????????
//        drawLongPressLine(canvas);
        //????????????????????????????????????
        drawTimeLine(canvas);

        //?????? ????????????
        canvas.restore();
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private int calculateWidth(String text) {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width() + 5;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private int calculateHeigh(String text) {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height() + 5;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private Rect calculateMaxMin(String text) {
        Rect rect = new Rect();
        mMaxMinPaint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * ?????????????????????index
     *
     * @param timeStamp
     * @return
     */
    private int calculateIndexFromDate(long timeStamp) {
        int index = 0;
        Log.i("count==", mAdapter.getCount() + "");
        for (int i = mAdapter.getCount(); i > mAdapter.getCount() - 150; i--) {
            Log.i("count==", "----");
            if (timeStamp > Long.valueOf(mAdapter.getTimeStamp(i))) {
                index = i + 1;
                Log.i("index==", "----" + index);
                break;
            }

        }

        return index;
    }


    /**
     * ???????????????????????????
     *
     * @param canvas
     */
    public void drawCircle(Canvas canvas) {
        orderEntityList.clear();
        //??????????????????????????????
        canvas.save();
        if (isFullScreen()) {
            canvas.translate(mTranslateX * mScaleX, 0);
        } else {
            canvas.translate(0, 0);
        }
        canvas.scale(1, 1);

        if (orderEntities != null)
//            for (int i = orderEntities.size() - 1; i > 0; i--) {
//                //???????????????????????????
//                if (isOption)
//                    drawOrderLine(orderEntities.get(i), canvas);
//                drawOrderCircle(orderEntities.get(i), canvas);
//                drawOrderAmount(orderEntities.get(i), canvas);
//            }
//            Collections.reverse(orderEntities); // ????????????
            for (int i = orderEntities.size() - 1; i >= 0; i--) {
                //???????????????????????????
                if (isOption)
                    drawOrderLine(orderEntities.get(i), canvas);
                drawOrderCircle(orderEntities.get(i), canvas);
                drawOrderAmount(orderEntities.get(i), canvas);
                orderEntityList.add(orderEntities.get(i));
            }

//        drawOrderCircle(orderEntities.get(0), canvas);



//        for (OrderEntity entity : orderEntities) {
//            //???????????????????????????
//            if (isOption)
//                drawOrderLine(entity, canvas);
//            drawOrderCircle(entity, canvas);
//            drawOrderAmount(entity, canvas);
//            orderEntityList.add(entity);
//
//        }


        //?????????????????????????????????
        drawTimeLineInfo(canvas);
    }


    private void drawOrderAmount(OrderEntity orderEntity, Canvas canvas) {
        String amount = orderEntity.getAmount() + "USDT";
        Log.i("orderEntity==", orderEntity.toString());
        int orderIndex = calculateIndexFromDate(orderEntity.getOrderTimeStamp());
        if (orderIndex == 0)
            return;

        float x = getX(orderIndex) * mScaleX - 35;
//            mOrderIndex -= 1;
        float openPrice = ((IKLine) getItem(orderIndex)).getOpenPrice();
        float textHeigh = calculateHeigh(amount);
        float y;
        if (isHaveCurrentIndexOrder(orderEntity))
            y = getMainY(openPrice) + 40;
        else y = getMainY(openPrice);

//        float y = getMainY(openPrice);
        float textWidth = mTextPaint.measureText(amount);
        float r = textHeigh * 2;
//        Log.i("x==", x + "");
//        canvas.drawCircle(x, getMainY(openPrice), 25, mWhitePaint);
        Path path = new Path();
        path.moveTo(x, y - r + 25);
        path.lineTo(x, y + 25);
        path.lineTo(x - textWidth - 10, y + 25);
        path.lineTo(x - textWidth - 10, y - r + 25);
        path.close();
        if (orderEntity.isUp()) {

            canvas.drawPath(path, mGreenPaint);
//            canvas.drawPath(path, mSelectorFramePaint);
            canvas.drawText(amount, x - textWidth - 5, fixTextY1(y), mSmallTextPaint);
//                canvas.drawBitmap(mBitmapUp, x - mBitmapUp.getWidth() / 2, getMainY(openPrice) - mBitmapUp.getHeight() / 2, mGreenPaint);

        } else {
            canvas.drawPath(path, mRedPaint);
//            canvas.drawPath(path, mSelectorFramePaint);
            canvas.drawText(amount, x - textWidth - 5, fixTextY1(y), mSmallTextPaint);
//
//                canvas.drawBitmap(mBitmapDown, x - mBitmapDown.getWidth() / 2, getMainY(openPrice) - mBitmapDown.getHeight() / 2, mGreenPaint);
        }
    }

    private boolean isHaveCurrentIndexOrder(OrderEntity orderEntity) {
        for (int i = 0; i < orderEntityList.size(); i++) {
            if (calculateIndexFromDate(orderEntity.getOrderTimeStamp()) == calculateIndexFromDate(orderEntityList.get(i).getOrderTimeStamp())) {
                return true;
            }

        }
        return false;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param orderEntity
     * @param canvas
     */
    private void drawOrderCircle(OrderEntity orderEntity, Canvas canvas) {

//        Log.i("orderEntity==", orderEntity.toString());
        int orderIndex = calculateIndexFromDate(orderEntity.getOrderTimeStamp());
        if (orderIndex == 0)
            return;
        float x = getX(orderIndex) * mScaleX;
//            mOrderIndex -= 1;
        float openPrice = ((IKLine) getItem(orderIndex)).getOpenPrice();


        float y;
        if (isHaveCurrentIndexOrder(orderEntity))
            y = getMainY(openPrice) + 40;
        else y = getMainY(openPrice);


//        Log.i("x==", x + "");
        canvas.drawCircle(x, y, 25, mWhitePaint);
        if (orderEntity.isUp()) {
            canvas.drawCircle(x, y, 20, mGreenPaint);
            Path path5 = new Path();
            path5.moveTo(x - 10, y + 10);
            path5.lineTo(x, y - 10);
            path5.lineTo(x + 10, y + 10);
            path5.close();
            canvas.drawPath(path5, mWhitePaint);
//                canvas.drawBitmap(mBitmapUp, x - mBitmapUp.getWidth() / 2, y - mBitmapUp.getHeight() / 2, mGreenPaint);

        } else {
            canvas.drawCircle(x, y, 20, mRedPaint);
            Path path5 = new Path();
            path5.moveTo(x - 10, y - 10);
            path5.lineTo(x, y + 10);
            path5.lineTo(x + 10, y - 10);
            path5.close();
            canvas.drawPath(path5, mWhitePaint);
//                canvas.drawBitmap(mBitmapDown, x - mBitmapDown.getWidth() / 2, y - mBitmapDown.getHeight() / 2, mGreenPaint);
        }


    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param orderEntity
     * @param canvas
     */
    private void drawOrderLine(OrderEntity orderEntity, Canvas canvas) {
        float startX = getX(calculateIndexFromDate(orderEntity.getOrderTimeStamp())) * mScaleX;
        float stopX = getX(calculateIndexFromDate(orderEntity.getOrderStopTimeStamp())) * mScaleX;
        float openPrice = ((IKLine) getItem(calculateIndexFromDate(orderEntity.getOrderTimeStamp()))).getOpenPrice();
        float startY = getMainY(openPrice);

        if (orderEntity.isUp()) {

            canvas.drawLine(startX, startY, stopX, startY, mGreenPaint);
        } else {
            canvas.drawLine(startX, startY, stopX, startY, mRedPaint);

        }

    }


    /**
     * ?????????????????????????????????
     * ??????????????????????????????????????????????????????
     *
     * @param canvas
     */
    private void drawTimeLineInfo(Canvas canvas) {
        if (isOption && timeItem > 0 && !TextUtils.isEmpty(period)) {
            float y;
            if (isShowChild) {
                y = mChildRect.bottom;
            } else {
                y = mVolRect.bottom;
            }

            if (period.equals("5s")) {
                //???????????????
                canvas.drawText(stopTime, getX(timeItem) * mScaleX + mBitmapStart.getWidth() / 2 + 5, y - mBitmapStart.getHeight() / 4, mBluePaint);
                //????????????????????????
                canvas.drawText(mStartCountTime + "s", (getX(timeItem - 6) * mScaleX - mBitmapStart.getWidth() / 2 - calculateWidth(mStartCountTime + "s") - 5),
                        y - mBitmapStart.getHeight() / 4, mBluePaint);

                //??????????????????
                if (holdTimeItem != 0 && holdTimeItem != timeItem) {
                    canvas.drawText(holdStopTime + "s", getX(holdTimeItem) * mScaleX - calculateWidth(holdStopTime) / 2, y - mBitmapStart.getHeight() / 4, mBluePaint);
                }

                canvas.drawBitmap(mBitmapStart, getX(timeItem - 6) * mScaleX - mBitmapStart.getWidth() / 2, y - mBitmapStart.getHeight(), mGreenPaint);
                canvas.drawBitmap(mBitmapStop, getX(timeItem) * mScaleX - mBitmapStart.getWidth() / 2, y - mBitmapStart.getHeight(), mGreenPaint);


            } else {
                canvas.drawText(stopTime, getX(timeItem) * mScaleX + 5, y, mBluePaint);
            }

        }
    }

    /**
     * ?????????
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //--------------?????????k????????????-------------
        if (mMainDraw != null) {
            canvas.drawText(formatValue(mMainMaxValue), mWidth - calculateWidth(formatValue(mMainMaxValue)), baseLine + mMainRect.top, mTextPaint);
            canvas.drawText(formatValue(mMainMinValue), mWidth - calculateWidth(formatValue(mMainMinValue)), mMainRect.bottom - textHeight + baseLine, mTextPaint);
            float rowValue = (mMainMaxValue - mMainMinValue) / mGridRows;
            float rowSpace = mMainRect.height() / mGridRows;
            for (int i = 1; i < mGridRows; i++) {
                String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                canvas.drawText(text, mWidth - calculateWidth(text), fixTextY(rowSpace * i + mMainRect.top), mTextPaint);
            }
        }
        //--------------?????????????????????-------------
        if (mVolDraw != null) {
            canvas.drawText(mVolDraw.getValueFormatter().format(mVolMaxValue, mDigit),
                    mWidth - calculateWidth(formatValue(mVolMaxValue)), mMainRect.bottom + baseLine, mTextPaint);
        }
        //--------------?????????????????????-------------
        if (mChildDraw != null) {
            canvas.drawText(mChildDraw.getValueFormatter().format(mChildMaxValue, mDigit),
                    mWidth - calculateWidth(formatValue(mChildMaxValue)), mVolRect.bottom + baseLine, mTextPaint);
        }

        //???????????????
        drawDateData(canvas, baseLine);
        //????????????????????????
        drawCurrentData(canvas, textHeight);
        //??????????????????k????????????
//        drawLongPressData(canvas, textHeight, baseLine);


    }


    /**
     * ???????????????
     *
     * @param canvas
     * @param baseLine
     */
    private void drawDateData(Canvas canvas, float baseLine) {
        float columnSpace = mWidth / mGridColumns;
        float y;
        if (isShowChild) {
            y = mChildRect.bottom + baseLine + 5;
        } else {
            y = mVolRect.bottom + baseLine + 5;
        }
        bottomY = y;

        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        boolean isOne = false;//??????????????????
        boolean isTwo = false;//??????????????????
        boolean isThree = false;//??????????????????

        for (int i = 1; i < mGridColumns; i++) {
            float translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                //??????k?????????????????????
                String text = mAdapter.getDate(index);
                //??????????????????
                if (isFullScreen()) {
                    canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTextPaint);
                } else {
                    if (i == 1) {
                        isOne = true;
                    }
                    if (i == 2 && !isOne) {
                        isTwo = true;
                    }

                    if (i == 3 && !isOne && !isTwo) {
                        isThree = true;
                    }

                    if (isOne) {
                        canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTextPaint);
                    }
                    if (isTwo) {
                        canvas.drawText(text, columnSpace * (i - 1) - mTextPaint.measureText(text) / 2, y, mTextPaint);
                    }
                    if (isThree) {
                        canvas.drawText(text, columnSpace * (i - 2) - mTextPaint.measureText(text) / 2, y, mTextPaint);
                    }
                }
            }
        }

        float translateX;
        //?????????????????????
        canvas.drawText(getAdapter().getDate(mStartIndex), 0, y, mTextPaint);
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = getAdapter().getDate(mStopIndex);
            //?????????????????????
            canvas.drawText(text, mWidth - mTextPaint.measureText(text), y, mTextPaint);
        }
    }

    /**
     * ??????????????????k????????????
     *
     * @param canvas
     * @param textHeight
     * @param baseLine
     */
    private void drawLongPressData(Canvas canvas, float textHeight, float baseLine) {
        if (isLongPress) {

            // ???Y??? ??????????????????k????????????
            IKLine point = (IKLine) getItem(mSelectedIndex);
            float w1 = ViewUtil.Dp2Px(getContext(), 5);
            float w2 = ViewUtil.Dp2Px(getContext(), 3);
            float r = textHeight / 2 + w2;
            float y = getMainY(point.getClosePrice());
            String text = formatValue(point.getClosePrice());
            float textWidth = mTextPaint.measureText(text);
            // k????????????
            canvas.drawLine(0, y, mWidth - textWidth - 1 - 2 * w1 - w2, y, mSelectedXLinePaint);
            if (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2) {//?????????
//                x = 1;
//                Path path = new Path();
//                path.moveTo(x, y - r);
//                path.lineTo(x, y + r);
//                path.lineTo(textWidth + 2 * w1, y + r);
//                path.lineTo(textWidth + 3 * w1, y);
//                path.lineTo(textWidth + 2 * w1, y - r);
//                path.close();
//                canvas.drawPath(path, mBluePaint);
////                canvas.drawPath(path, mSelectorFramePaint);
//                canvas.drawText(text, x + w1, fixTextY1(y), mSmallTextPaint);
            } else {//?????????
//                x = mWidth - textWidth - 1 - 2 * w1 - w2;
//                Path path = new Path();
//                path.moveTo(x - w1, y);
//                path.lineTo(x + w2, y + r);
//                path.lineTo(mWidth - 2, y + r);
//                path.lineTo(mWidth - 2, y - r);
//                path.lineTo(x + w2, y - r);
//                path.close();
//                canvas.drawPath(path, mBluePaint);
////                canvas.drawPath(path, mSelectorFramePaint);
//                canvas.drawText(text, x + w1 + w2, fixTextY1(y), mSmallTextPaint);
            }


            // ?????????k????????????
            String date = mAdapter.getDate(mSelectedIndex);
            textWidth = mTextPaint.measureText(date);
            r = textHeight / 2;
            if (isFullScreen())
                x = translateXtoX(getX(mSelectedIndex));
            else
                x = getX(mSelectedIndex);
            if (isShowChild) {
                y = mChildRect.bottom;
            } else {
                y = mVolRect.bottom;
            }

            if (x < textWidth + 2 * w1) {
                x = 1 + textWidth / 2 + w1;
            } else if (mWidth - x < textWidth + 2 * w1) {
                x = mWidth - 1 - textWidth / 2 - w1;
            }
            if (isFullScreen()) {//?????????????????????
                canvas.drawRect(x - textWidth / 2 - w1, y, x + textWidth / 2 + w1
                        , y + baseLine + r, mSelectPointPaint);
                canvas.drawRect(x - textWidth / 2 - w1, y, x + textWidth / 2 + w1,
                        y + baseLine + r, mSelectorFramePaint);
                canvas.drawText(date, x - textWidth / 2, y + baseLine + 5, mTextPaint);

            } else {
                canvas.drawRect(x - textWidth / 2 - w1, y, x + textWidth / 2 + w1, y + baseLine + r, mSelectPointPaint);
                canvas.drawRect(x - textWidth / 2 - w1, y, x + textWidth / 2 + w1, y + baseLine + r, mSelectorFramePaint);
                canvas.drawText(date, x - textWidth / 2, y + baseLine + 5, mTextPaint);

            }
        }
    }

    /**
     * ????????????????????????
     */
    @SuppressLint("ResourceAsColor")
    private void drawCurrentData(Canvas canvas, float textHeight) {


        // ???Y???
        IKLine point = (IKLine) getItem(mAdapter.getCount() - RANG_ITEM - 1);

//        String s = "   ???:" + point.getHighPrice() + "   ???:" + point.getOpenPrice() + "   ???:" + point.getLowPrice() + "   ???:" + point.getClosePrice();
//        setNewInfo("  " + mAdapter.getDate(mItemCount - RANG_ITEM - 1) + s);
//        setNewPosition(mAdapter.getCount() - RANG_ITEM - 1);

//        mSmallTextPaint.setColor(getResources().getColor(R.color.chart_white));

        float w1 = ViewUtil.Dp2Px(getContext(), 5);
        float w2 = ViewUtil.Dp2Px(getContext(), 3);
        float r = textHeight / 2 + w2;
        float y = getMainY(point.getClosePrice());
        float x;
        String text = formatValue(point.getClosePrice());
        float textWidth = mTextPaint.measureText(text);
        if (translateXtoX(getX(mAdapter.getCount() - 1)) < getChartWidth() / 2) {
            x = 1;
            Path path = new Path();
            path.moveTo(x, y - r);
            path.lineTo(x, y + r);
            path.lineTo(textWidth + 2 * w1, y + r);
            path.lineTo(textWidth + 2 * w1, y - r);
            path.close();
            canvas.drawPath(path, mBluePaint);
//            canvas.drawPath(path, mSelectorFramePaint);
            canvas.drawText(text, x + w1, fixTextY1(y), mSmallTextPaint);
        } else {

            x = mWidth - textWidth - 1 - 2 * w1 - w2;
            Path path = new Path();
            path.moveTo(x - w1, y);
            path.lineTo(x + w2, y + r);
            path.lineTo(mWidth - 2, y + r);
            path.lineTo(mWidth - 2, y - r);
            path.lineTo(x + w2, y - r);
            path.close();
            canvas.drawPath(path, mBluePaint);
//            canvas.drawPath(path, mSelectorFramePaint);
            canvas.drawText(text, x + w1 + w2, fixTextY1(y), mSmallTextPaint);
        }


        Log.i("textWidth", textWidth + "--w1--" + w1);
        //?????????????????????
        canvas.drawLine(0, y, x, y, mSelectedXLinePaint);

//        canvas.drawBitmap(bitmapPoint, getX(mAdapter.getCount() - 50 - 1) * mScaleX, y - bitmapPoint.getHeight() / 2, mSelectedXLinePaint);

    }

    /**
     * ?????????????????????
     *
     * @param canvas
     */
    private void drawLongPressLine(Canvas canvas) {
        //????????????
        if (isLongPress) {
            IKLine point = (IKLine) getItem(mSelectedIndex);
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getClosePrice());
            // k????????????
            canvas.drawLine(x, mMainRect.top, x, mMainRect.bottom, mSelectedYLinePaint);
            // k????????????
//            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedXLinePaint);
            // ???????????????
            canvas.drawLine(x, mMainRect.bottom, x, mVolRect.bottom, mSelectedYLinePaint);
            if (mChildDraw != null) {
                // ???????????????
                canvas.drawLine(x, mVolRect.bottom, x, mChildRect.bottom, mSelectedYLinePaint);
            }

            Log.i("mTranslateX", mTranslateX + "--mWidth--" + mWidth);
        }
    }


    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param canvas
     */
    private void drawTimeLine(Canvas canvas) {
        if (isOption && timeItem > 0 && !TextUtils.isEmpty(period)) {
            if (period.equals("5s")) {
                float startX = getX(timeItem - 6);
                float stopX = getX(timeItem);
                float y;
                if (isShowChild) {
                    y = mChildRect.bottom - mBitmapStart.getHeight();
                } else {
                    y = mVolRect.bottom - mBitmapStart.getHeight();
                }

                int mSmallWidth = 20;//????????????
                int interval = 20;
                int number = (int) (y / mSmallWidth);

//                canvas.drawBitmap(mBitmapStart, startX, getHeight(), mGreenPaint);


                if (holdTimeItem != 0) {
                    //??????????????????????????????
                    float holdX = getX(holdTimeItem);
//                    canvas.drawLine(holdX, 0, holdX, getHeight(), mBluePaint);//???????????????
                    for (int i1 = 0; i1 < number; i1++) {
                        if ((i1 + 1) * mSmallWidth + interval * i1 < y) {
                            canvas.drawLine(holdX, i1 * mSmallWidth + interval * i1, holdX, (i1 + 1) * mSmallWidth + interval * i1, mBluePaint);
                        }
                    }
                }

                Log.i("x==", x + "");
//                canvas.drawLine(x, 0, x, getHeight(), mTimePaint);//?????????
                for (int i1 = 0; i1 < number; i1++) {
                    if ((i1 + 1) * mSmallWidth + interval * i1 < y) {
                        //???????????????????????????????????????
                        canvas.drawLine(startX, i1 * mSmallWidth + interval * i1, startX, (i1 + 1) * mSmallWidth + interval * i1, mTimePaint);
                        canvas.drawLine(stopX, i1 * mSmallWidth + interval * i1, stopX, (i1 + 1) * mSmallWidth + interval * i1, mBluePaint);
                    }
                }

//                Rect srcRect = new Rect(0, 0, mBitmapStart.getHeight(), mBitmapStart.getWidth());
////                Rect destRect = getBitmapRect(mBitmap); // ??????????????????bitmap???????????????
//                Rect destRect = new Rect(mBitmapStart.getWidth()+50,0,mBitmapStart.getWidth()+50+mBitmapStart.getWidth(),mBitmapStart.getHeight());
////
//                canvas.drawBitmap(mBitmapStart, srcRect, destRect, mBluePaint);
//                canvas.drawBitmap(mBitmapStart, startX, getHeight(), mBluePaint);

//                canvas.drawLine(x, 0, x, getHeight(), mBluePaint);//?????????
//                for (int i1 = 0; i1 < number; i1++) {
//                    if ((i1 + 1) * mSmallWidth + interval * i1 < mMainRect.bottom) {
//                        canvas.drawLine(stopX, i1 * mSmallWidth + interval * i1, stopX, (i1 + 1) * mSmallWidth + interval * i1, mBluePaint);
//                    }
//                }


            } else {
                float x = getX(timeItem);
                Log.i("x==", x + "");
                canvas.drawLine(x, 0, x, getHeight(), mTimePaint);//?????????

                canvas.drawLine(x, 0, x, getHeight(), mBluePaint);//?????????
            }

        }

    }

//    }

    /**
     * ?????????
     *
     * @param canvas
     */
    private void drawMaxAndMin(Canvas canvas) {
        if (!mainDraw.isLine()) {
            IKLine maxEntry = null, minEntry = null;
            boolean firstInit = true;


            //???????????????????????????
            float x = translateXtoX(getX(mMainMinIndex));
            float y = getMainY(mMainLowMinValue);
            String LowString = "?????? " + Float.toString(mMainLowMinValue);
            //??????????????????
            //??????????????????
            int lowStringWidth = calculateMaxMin(LowString).width();
            int lowStringHeight = calculateMaxMin(LowString).height();
            if (x < getWidth() / 2) {
                //?????????
                canvas.drawText(LowString, x, y + lowStringHeight / 2, mMaxMinPaint);
            } else {
                //?????????
                LowString = Float.toString(mMainLowMinValue) + " ??????";
                canvas.drawText(LowString, x - lowStringWidth, y + lowStringHeight / 2, mMaxMinPaint);
            }

            x = translateXtoX(getX(mMainMaxIndex));
            y = getMainY(mMainHighMaxValue);

            String highString = "?????? " + Float.toString(mMainHighMaxValue);
            int highStringWidth = calculateMaxMin(highString).width();
            int highStringHeight = calculateMaxMin(highString).height();
            if (x < getWidth() / 2) {
                //?????????
                canvas.drawText(highString, x, y + highStringHeight / 2, mMaxMinPaint);
            } else {
                //?????????
                highString = Float.toString(mMainHighMaxValue) + " ??????";
                canvas.drawText(highString, x - highStringWidth, y + highStringHeight / 2, mMaxMinPaint);
            }

        }
    }

    /**
     * ??????
     *
     * @param canvas
     * @param position ?????????????????????
     */
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight;
                mMainDraw.drawText(canvas, this, position, 0, y);
            }
            if (mVolDraw != null) {
                float y = mMainRect.bottom + baseLine;
                mVolDraw.drawText(canvas, this, position, 0, y);
            }
            if (mChildDraw != null) {
                float y = mVolRect.bottom + baseLine;
                mChildDraw.drawText(canvas, this, position, 0, y);
            }
        }
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * ????????????
     */
    public String formatValue(float value) {
        if (getValueFormatter() == null) {
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value, mDigit);
    }

    /**
     * ???????????????????????????
     */
    public void notifyChanged() {
        if (isShowChild && mChildDrawPosition == -1) {
            mChildDraw = mChildDraws.get(0);
            mChildDrawPosition = 0;
        }
        if (mItemCount != 0) {
            mDataLen = (mItemCount - 1) * mPointWidth;
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        } else {
            setScrollX(0);
        }
        invalidate();
    }

    /**
     * MA/BOLL???????????????
     *
     * @param status MA/BOLL/NONE
     */
    public void changeMainDrawType(Status status) {
        if (mainDraw != null && mainDraw.getStatus() != status) {
            mainDraw.setStatus(status);
            invalidate();
        }
    }

    private void calculateSelectedX(float x) {
        //??????k?????????????????????
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        Log.i("mSelectedIndex", mSelectedIndex + "");

        if (mSelectedIndex > mItemCount - RANG_ITEM - 1) {
            //????????????????????????????????????
            mSelectedIndex = mItemCount - RANG_ITEM - 1;
        }

        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        if (isFullScreen())
            calculateSelectedX(e.getX());
        else
            calculateSelectedX(translateXtoX(e.getX()));
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }


    /**
     * ???????????????????????????
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mMainMaxValue = Float.MIN_VALUE;
        mMainMinValue = Float.MAX_VALUE;
        mVolMaxValue = Float.MIN_VALUE;
        mVolMinValue = Float.MAX_VALUE;
        mChildMaxValue = Float.MIN_VALUE;
        mChildMinValue = Float.MAX_VALUE;
        //??????????????????
        if (isFullScreen()) {
            mStartIndex = indexOfTranslateX(xToTranslateX(0));
//            mStartIndex = (int) (mStartIndex + 10 / mScaleX) + 1;
        } else mStartIndex = 0;
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        mMainMaxIndex = mStartIndex;
        mMainMinIndex = mStartIndex;
        mMainHighMaxValue = Float.MIN_VALUE;
        mMainLowMinValue = Float.MAX_VALUE;
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IKLine point = (IKLine) getItem(i);
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getMaxValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinValue(point));
                if (mMainHighMaxValue != Math.max(mMainHighMaxValue, point.getHighPrice())) {
                    mMainHighMaxValue = point.getHighPrice();
                    mMainMaxIndex = i;
                }
                if (mMainLowMinValue != Math.min(mMainLowMinValue, point.getLowPrice())) {
                    mMainLowMinValue = point.getLowPrice();
                    mMainMinIndex = i;
                }
            }
            if (mVolDraw != null) {
                mVolMaxValue = Math.max(mVolMaxValue, mVolDraw.getMaxValue(point));
                mVolMinValue = Math.min(mVolMinValue, mVolDraw.getMinValue(point));
            }
            if (mChildDraw != null) {
                mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
            }
        }
        if (mMainMaxValue != mMainMinValue) {
            float padding = (mMainMaxValue - mMainMinValue) * 0.05f;
            mMainMaxValue += padding;
            mMainMinValue -= padding;
        } else {
            //?????????????????????????????????????????? ???????????????????????? ???????????????
            mMainMaxValue += Math.abs(mMainMaxValue * 0.05f);
            mMainMinValue -= Math.abs(mMainMinValue * 0.05f);
            if (mMainMaxValue == 0) {
                mMainMaxValue = 1;
            }
        }

        if (Math.abs(mVolMaxValue) < 0.01) {
            mVolMaxValue = 15.00f;
        }

        if (Math.abs(mChildMaxValue) < 0.01 && Math.abs(mChildMinValue) < 0.01) {
            mChildMaxValue = 1f;
        } else if (mChildMaxValue.equals(mChildMinValue)) {
            //?????????????????????????????????????????? ???????????????????????? ???????????????
            mChildMaxValue += Math.abs(mChildMaxValue * 0.05f);
            mChildMinValue -= Math.abs(mChildMinValue * 0.05f);
            if (mChildMaxValue == 0) {
                mChildMaxValue = 1f;
            }
        }

        if (isWR) {
            mChildMaxValue = 0f;
            if (Math.abs(mChildMinValue) < 0.01)
                mChildMinValue = -10.00f;
        }
        mMainScaleY = mMainRect.height() * 1f / (mMainMaxValue - mMainMinValue);
        mVolScaleY = mVolRect.height() * 1f / (mVolMaxValue - mVolMinValue);
        if (mChildRect != null)
            mChildScaleY = mChildRect.height() * 1f / (mChildMaxValue - mChildMinValue);
        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    /**
     * ??????????????????
     *
     * @param startX    ?????????????????????
     * @param stopX     ???????????????
     * @param stopX     ?????????????????????
     * @param stopValue ???????????????
     */
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        Log.i("startX--", startX + "");
        Log.i("startValue--", startValue + "");
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);


//        canvas.drawBitmap(mBitmap, (float) 7488.0, getMainY((float) 10347.1), paint);
//        RectF rectf = new RectF(0, 0, 200, 200);   //w???h????????????????????????????????????????????????????????????????????????
//        canvas.drawBitmap(mBitmap, null, rectf, paint);
    }


    /**
     * ????????????????????????
     *
     * @param startX    ?????????????????????
     * @param stopX     ???????????????
     * @param stopX     ?????????????????????
     * @param stopValue ???????????????
     */
    public void drawMainMinuteLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        Path path5 = new Path();
        path5.moveTo(startX, displayHeight + mTopPadding + mBottomPadding);
        path5.lineTo(startX, getMainY(startValue));
        path5.lineTo((stopX), getMainY(stopValue));
        path5.lineTo(stopX, displayHeight + mTopPadding + mBottomPadding);
        path5.close();
        canvas.drawPath(path5, paint);
    }

    /**
     * ??????????????????
     *
     * @param startX     ?????????????????????
     * @param startValue ???????????????
     * @param stopX      ?????????????????????
     * @param stopValue  ???????????????
     */
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    /**
     * ??????????????????
     *
     * @param startX     ?????????????????????
     * @param startValue ???????????????
     * @param stopX      ?????????????????????
     * @param stopValue  ???????????????
     */
    public void drawVolLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getVolY(startValue), stopX, getVolY(stopValue), paint);
    }

    /**
     * ????????????????????????
     *
     * @param position ?????????
     * @return
     */
    public Object getItem(int position) {
        if (mAdapter != null) {
            return mAdapter.getItem(position);
        } else {
            return null;
        }
    }


    /**
     * ??????????????????x??????
     *
     * @param position ?????????
     * @return
     */
    public float getX(int position) {
        return position * mPointWidth;
    }

    /**
     * ???????????????
     *
     * @return
     */
    public IAdapter getAdapter() {
        return mAdapter;
    }

    private static final String TAG = "BaseKLineChartView";

    /**
     * ??????????????????
     *
     * @param position
     */
    public void setChildDraw(int position) {
        if (mChildDrawPosition != position) {
            if (!isShowChild) {
                Log.i(TAG, "setChildDraw: " + position);
                isShowChild = true;
            }
            if (position != 3) {

                mChildDraw = mChildDraws.get(position);
                mChildDrawPosition = position;
            }
            isWR = position == 5;

        }
        if (position == 3) {//??????VOL
            isShowVOL = true;
            isShowChild = false;
            mVolDraw = mVolDraws.get(0);
            mChildDrawPosition = -1;
            mChildDraw = null;

        } else {
            isShowChild = true;
            isShowVOL = false;
            mVolDraw = null;
        }
        initRect();
        invalidate();
    }

    /**
     * ????????????
     */
    public void hideChildDraw() {
        mChildDrawPosition = -1;
        isShowChild = false;
        isShowVOL = false;
        mChildDraw = null;
        mVolDraw = null;
        initRect();
        invalidate();
    }

    /**
     * ??????????????????????????????
     *
     * @param childDraw IChartDraw
     */
    public void addChildDraw(IChartDraw childDraw) {
        mChildDraws.add(childDraw);
    }

    public void addVolDraw(IChartDraw mVolDraw) {
        mVolDraws.add(mVolDraw);
        this.mVolDraw = mVolDraw;
    }

    /**
     * scrollX ????????? TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }

    /**
     * ??????ValueFormatter
     *
     * @return
     */
    public IValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    /**
     * ??????ValueFormatter
     *
     * @param valueFormatter value????????????
     */
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.mValueFormatter = valueFormatter;
    }

    /**
     * ??????DatetimeFormatter
     *
     * @return ??????????????????
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return mDateTimeFormatter;
    }

    /**
     * ??????dateTimeFormatter
     *
     * @param dateTimeFormatter ??????????????????
     */
    public void setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        mDateTimeFormatter = dateTimeFormatter;
    }

    /**
     * ???????????????
     *
     * @param date
     */
    public String formatDateTime(Date date) {
        if (getDateTimeFormatter() == null) {
            setDateTimeFormatter(new TimeFormatter());
        }
        return getDateTimeFormatter().format(date);
    }

    /**
     * ?????????????????? IChartDraw
     *
     * @return IChartDraw
     */
    public IChartDraw getMainDraw() {
        return mMainDraw;
    }

    /**
     * ?????????????????? IChartDraw
     *
     * @param mainDraw IChartDraw
     */
    public void setMainDraw(IChartDraw mainDraw) {
        mMainDraw = mainDraw;
        this.mainDraw = (MainDraw) mMainDraw;
    }

    public IChartDraw getVolDraw() {
        return mVolDraw;
    }

    public void setVolDraw(IChartDraw mVolDraw) {
        this.mVolDraw = mVolDraw;
    }

    /**
     * ????????????????????????index
     *
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (end == start) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }
    /**
     * price ellipse Red
     *
     * @param canvas       canvas
     * @param x            x
     * @param y1           y1
     * @param y2           y2
     * @param lastPrice    lastPrice
     */
    protected void renderBitmapEllipse(Canvas canvas,float x,float y1,float y2,float lastPrice){
        mBitmapEllipseRed = zoomImg(mBitmapEllipseRed,50,50);
        mBitmapVectorDown = zoomImg(mBitmapVectorDown,30,30);
//x  stopX-lineEndRadius/2 * lineEndMaxMultiply - 10
        Rect srcRect = new Rect(
                new BigDecimal(x).intValue()
                ,0
                , new BigDecimal(mBitmapEllipseRed.getHeight() / 2).intValue()
                , new BigDecimal(mBitmapEllipseRed.getWidth() / 2).intValue());
        Rect destRect = new Rect(
                new BigDecimal(x).intValue()
                , 0
                ,new BigDecimal(mBitmapEllipseRed.getWidth() + mBitmapEllipseRed.getWidth() / 2).intValue()
                , new BigDecimal(getMainY(lastPrice)).intValue());

        Rect srcDownRect = new Rect(
                new BigDecimal(x).intValue()
                ,0
                , new BigDecimal(mBitmapVectorDown.getHeight() / 2).intValue()
                , new BigDecimal(mBitmapVectorDown.getWidth() / 2).intValue());
        Rect destDownRect = new Rect(
                new BigDecimal(x).intValue()
                , 0
                ,new BigDecimal(mBitmapVectorDown.getWidth() + mBitmapVectorDown.getWidth() / 2).intValue()
                , new BigDecimal(getMainY(lastPrice)).intValue());
//y getMainY(lastPrice)-10
        canvas.drawBitmap(mBitmapEllipseRed, srcRect, destRect, mEllipsePaint);
        canvas.drawBitmap(mBitmapEllipseRed, x, y1, mEllipsePaint);

        canvas.drawBitmap(mBitmapVectorDown, srcDownRect, destDownRect, mEllipsePaint);
        //x stopX-lineEndRadius/2 * lineEndMaxMultiply
        //y getMainY(lastPrice)+mBitmapEllipseRed.getHeight()-15
        canvas.drawBitmap(mBitmapVectorDown, x + 10, y2, mEllipsePaint);
    }
    /**
     * price ellipse green
     *
     * @param canvas       canvas
     * @param x            x
     * @param y1           y1
     * @param y2           y2
     * @param lastPrice    lastPrice
     */
    protected void renderBitmapEllipseGreen(Canvas canvas,float x,float y1,float y2,float lastPrice){
        mBitmapEllipseGreen = zoomImg(mBitmapEllipseGreen,50,50);
        mBitmapVectorUp = zoomImg(mBitmapVectorUp,30,30);
//x  stopX-lineEndRadius/2 * lineEndMaxMultiply - 10
        Rect srcRect = new Rect(
                new BigDecimal(x).intValue()
                ,0
                , new BigDecimal(mBitmapEllipseGreen.getHeight() / 2).intValue()
                , new BigDecimal(mBitmapEllipseGreen.getWidth() / 2).intValue());
        Rect destRect = new Rect(
                new BigDecimal(x).intValue()
                , 0
                ,new BigDecimal(mBitmapEllipseGreen.getWidth() + mBitmapEllipseGreen.getWidth() / 2).intValue()
                , new BigDecimal(getMainY(lastPrice)).intValue());

        Rect srcDownRect = new Rect(
                new BigDecimal(x).intValue()
                ,0
                , new BigDecimal(mBitmapVectorUp.getHeight() / 2).intValue()
                , new BigDecimal(mBitmapVectorUp.getWidth() / 2).intValue());
        Rect destDownRect = new Rect(
                new BigDecimal(x).intValue()
                , 0
                ,new BigDecimal(mBitmapVectorUp.getWidth() + mBitmapVectorUp.getWidth() / 2).intValue()
                , new BigDecimal(getMainY(lastPrice)).intValue());
//y getMainY(lastPrice)-10
        canvas.drawBitmap(mBitmapEllipseGreen, srcRect, destRect, mEllipsePaint);
        canvas.drawBitmap(mBitmapEllipseGreen, x, y1, mEllipsePaint);

        canvas.drawBitmap(mBitmapVectorUp, srcDownRect, destDownRect, mEllipsePaint);
        //x stopX-lineEndRadius/2 * lineEndMaxMultiply
        //y getMainY(lastPrice)+mBitmapEllipseRed.getHeight()-15
        canvas.drawBitmap(mBitmapVectorUp, x + 10, y2, mEllipsePaint);
    }
    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    /**
     * ?????????????????????
     */
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    /**
     * ????????????
     */
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    /**
     * ??????????????????
     */
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    /**
     * ??????????????????
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
    }

    /**
     * ??????????????????
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
    }

    /**
     * ???????????????????????????
     *
     * @param digit
     */
    public void setDigit(int digit) {
        mDigit = digit;
        if (mainDraw != null)
            mainDraw.setmDigit(digit);
    }

    /**
     * view??????x?????????TranslateX
     *
     * @param x
     * @return
     */
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    /**
     * translateX?????????view??????x
     *
     * @param translateX
     * @return
     */
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    /**
     * ????????????padding
     */
    public float getTopPadding() {
        return mTopPadding;
    }

    /**
     * ????????????padding
     */
    public float getChildPadding() {
        return mChildPadding;
    }

    /**
     * ?????????????????????padding
     */
    public float getmChildScaleYPadding() {
        return mChildPadding;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    public int getChartWidth() {
        return mWidth;
    }

    /**
     * ????????????
     */
    public boolean isLongPress() {
        return isLongPress;
    }

    /**
     * ??????????????????
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public Rect getChildRect() {
        return mChildRect;
    }

    public Rect getVolRect() {
        return mVolRect;
    }

    /**
     * ??????????????????
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    public void onSelectedChanged(BaseKLineChartView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }


    /**
     * ???????????????????????????????????????
     */
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }

    /**
     * ????????????padding
     *
     * @param topPadding
     */
    public void setTopPadding(int topPadding) {
        mTopPadding = topPadding;
    }

    /**
     * ????????????padding
     *
     * @param bottomPadding
     */
    public void setBottomPadding(int bottomPadding) {
        mBottomPadding = bottomPadding;
    }

    /**
     * ?????????????????????
     */
    public void setGridLineWidth(float width) {
        this.mGridPaint.setStrokeWidth(width);
    }

    /**
     * ?????????????????????
     */
    public void setGridLineColor(int color) {
        this.mGridPaint.setColor(color);
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedXLineWidth(float width) {
        mSelectedXLinePaint.setStrokeWidth(width);
        mGreenPaint.setStrokeWidth(width);
        mRedPaint.setStrokeWidth(width);
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedXLineColor(int color) {
        mSelectedXLinePaint.setColor(color);
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedYLineWidth(float width) {
        mSelectedYLinePaint.setStrokeWidth(width);
    }

    /**
     * ???????????????????????????
     */
    public void setSelectedYLineColor(int color) {
        mSelectedYLinePaint.setColor(color);
    }

    /**
     * ??????????????????
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);

    }

    /**
     * ??????????????????
     */
    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        mBluePaint.setTextSize(textSize);
        mSmallTextPaint.setTextSize(textSize);
    }

    /**
     * ???????????????/?????????????????????
     */
    public void setMTextColor(int color) {
        mMaxMinPaint.setColor(color);
    }

    /**
     * ???????????????/?????????????????????
     */
    public void setMTextSize(float textSize) {
        mMaxMinPaint.setTextSize(textSize);
    }

    /**
     * ??????????????????
     */
    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }

    /**
     * ????????????point ???????????????
     */
    public void setSelectPointColor(int color) {
        mSelectPointPaint.setColor(color);
    }

    /**
     * ???????????????????????????
     */
    public interface OnSelectedChangedListener {
        /**
         * ?????????????????????
         *
         * @param view  ??????view
         * @param point ????????????
         * @param index ??????????????????
         */
        void onSelectedChanged(BaseKLineChartView view, Object point, int index);
    }

    /**
     * ??????????????????
     */
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     * ??????????????????
     */
    public float getLineWidth() {
        return mLineWidth;
    }

    /**
     * ?????????????????????
     */
    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    /**
     * ????????????????????????
     */
    public void setPointWidth(float pointWidth) {
        mPointWidth = pointWidth;
    }

    public float getmPointWidth() {
        return mPointWidth;
    }

    public Paint getGridPaint() {
        return mGridPaint;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public int getDisplayHeight() {
        return displayHeight + mTopPadding + mBottomPadding;
    }
}
