package com.gizwits.opensource.appkit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.gizwits.opensource.appkit.R;



/**
 * Created by qiaoning on 2017/8/9.
 */

public class CircleProgress extends View {

    //小圆的个数
    private int numOfCircles;
    //最大圆半径
    private int maxRadius;
    //最小圆半径
    private int minRadius;
    //旋转速度
    private int rotateSpeedInMillis;
    //是否顺时针旋转
    private boolean isClockwise;
    //小圆的颜色
    private int circleColor;

    private Paint paint;

    private float rotateDegrees;
    private int numOfRotate;

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

        numOfCircles = array.getInt(R.styleable.CircleProgress_numOfCircles, 10);
        maxRadius = array.getDimensionPixelSize(R.styleable.CircleProgress_maxRadius, dp2px(10));
        minRadius = array.getDimensionPixelSize(R.styleable.CircleProgress_minRadius, dp2px(2));
        rotateSpeedInMillis = array.getInt(R.styleable.CircleProgress_rotateSpeedInMillis, 200);
        isClockwise = array.getBoolean(R.styleable.CircleProgress_isClockwise, true);
        circleColor = array.getColor(R.styleable.CircleProgress_circleColor, Color.BLACK);

        array.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(circleColor);

        //view每次旋转的角度
        rotateDegrees = 360 / numOfCircles;
        //旋转的周期为小圆的个数
        numOfRotate = 0;

    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //按一定角度旋转画布canvas，当旋转周期==小圆个数时重置
        if (numOfRotate == numOfCircles) {
            numOfRotate = 0;
        }

        //根据旋转的方向旋转画布canvas，然后再画小圆
        if (isClockwise) {
            canvas.rotate(rotateDegrees * numOfRotate, getWidth() / 2, getHeight() / 2);
        } else {
            canvas.rotate(-rotateDegrees * numOfRotate, getWidth() / 2, getHeight() / 2);
        }

        //记录旋转的次数，下次invalidat()重绘时就可以使用新的角度旋转canvas，使小圆产生旋转的感觉
        numOfRotate++;

        //取View最短边，并减去最大圆的半径，得到所有圆所在的圆路径的半径
        int radius = (getWidth() > getHeight() ? getHeight() : getWidth()) / 2 - maxRadius;
        //每个小圆的半径增量
        float radiusIncrement = (float) (maxRadius - minRadius) / numOfCircles;
        //每隔多少度绘制一个小圆,弧度制
        double angle = 2 * Math.PI / numOfCircles;

        //按位置画小圆
        //每个小圆的位置可以由正弦余弦函数求出，并且每个小圆半径依次递增，若反方向则依次递减
        if (isClockwise) {
            for (int i = 0; i < numOfCircles; i++) {
                float x = (float) (getWidth() / 2 + Math.cos(i * angle) * radius);
                float y = (float) (getHeight() / 2 - Math.sin(i * angle) * radius);
                canvas.drawCircle(x, y, maxRadius - radiusIncrement * i, paint);
            }
        } else {
            for (int i = 0; i < numOfCircles; i++) {
                float x = (float) (getWidth() / 2 + Math.cos(i * angle) * radius);
                float y = (float) (getHeight() / 2 - Math.sin(i * angle) * radius);
                canvas.drawCircle(x, y, minRadius + radiusIncrement * i, paint);
            }
        }


        //旋转间隔，即progressBar的旋转速度
        postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                invalidate();
            }
        }, rotateSpeedInMillis);
    }

    //dp转px函数
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
