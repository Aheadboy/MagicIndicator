package net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.text.MessageFormat;
import java.util.List;

/**
 * 带有小尖角的直线指示器
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class TriangularPagerIndicator extends View implements IPagerIndicator {
    private final static String TAG = "TriangularPagerIndicato";
    private List<PositionData> mPositionDataList;
    private Paint mPaint;
    private int mLineHeight;
    private int mLineColor;
    private int mTriangleHeight;
    private int mTriangleWidth;
    private boolean mReverse;//倒三角
    private float mYOffset;

    private Path mPath = new Path();
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private float mAnchorX;//Anchor锚//在三角形底边的中心

    public TriangularPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mLineHeight = UIUtil.dip2px(context, 3);
        mTriangleWidth = UIUtil.dip2px(context, 14);
        mTriangleHeight = UIUtil.dip2px(context, 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, MessageFormat.format("onDraw invoke :{0}", ""));

        mPaint.setColor(mLineColor);

        Log.i(TAG, MessageFormat.format("mReverse:{0}",mReverse));

        //region 画底部的线。
        if (mReverse) {
            canvas.drawRect(0, getHeight() - mYOffset - mTriangleHeight, getWidth(), getHeight() - mYOffset - mTriangleHeight + mLineHeight, mPaint);
            Log.e(TAG, MessageFormat.format("getHeight:{0} px", getHeight()));
            Log.e(TAG, MessageFormat.format("mYOffset:{0} px", mYOffset));
            Log.e(TAG, MessageFormat.format("mTriangleHeight:{0} px", mTriangleHeight));
            Log.e(TAG, MessageFormat.format("top:{0} px", getHeight() - mYOffset - mTriangleHeight));
            Log.e(TAG, MessageFormat.format("right:{0} px", getWidth()));
            Log.e(TAG, MessageFormat.format("bottom:{0} px", getHeight() - mYOffset - mTriangleHeight + mLineHeight));

        } else {
            Log.e(TAG, MessageFormat.format("getHeight:{0} px", getHeight()));
            Log.e(TAG, MessageFormat.format("mYOffset:{0} px", mYOffset));
            Log.e(TAG, MessageFormat.format("mTriangleHeight:{0} px", mTriangleHeight));
            Log.e(TAG, MessageFormat.format("top:{0} px", getHeight() - mLineHeight - mYOffset));
            Log.e(TAG, MessageFormat.format("right:{0} px", getWidth()));
            Log.e(TAG, MessageFormat.format("bottom:{0} px", getHeight() - mYOffset));
            canvas.drawRect(0, getHeight() - mLineHeight - mYOffset, getWidth(), getHeight() - mYOffset, mPaint);
        }
        //endregion
        mPath.reset();

        //region 画三角形
        if (mReverse) {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, getHeight() - mYOffset - mTriangleHeight);
            mPath.lineTo(mAnchorX, getHeight() - mYOffset);
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset - mTriangleHeight);
        } else {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, getHeight() - mYOffset);
            mPath.lineTo(mAnchorX, getHeight() - mTriangleHeight - mYOffset);
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset);
        }
        mPath.close();//起始点闭合
        //endregion
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.e(TAG, MessageFormat.format("position:{0}", position));
        Log.e(TAG, MessageFormat.format("positionOffset:{0}", positionOffset));
        Log.e(TAG, MessageFormat.format("positionOffsetPixels:{0}", positionOffsetPixels));

        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        float leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        mAnchorX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        Log.e(TAG, MessageFormat.format("current:{0};next:{1};leftX:{2};rightX:{3};", current.toString(),next.toString(),leftX,rightX));
        invalidate();//触发onDraw方法
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    //region getter and setter
    public int getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(int lineHeight) {
        mLineHeight = lineHeight;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getTriangleHeight() {
        return mTriangleHeight;
    }

    public void setTriangleHeight(int triangleHeight) {
        mTriangleHeight = triangleHeight;
    }

    public int getTriangleWidth() {
        return mTriangleWidth;
    }

    public void setTriangleWidth(int triangleWidth) {
        mTriangleWidth = triangleWidth;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public boolean isReverse() {
        return mReverse;
    }

    public void setReverse(boolean reverse) {
        mReverse = reverse;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }
    //endregion
}
