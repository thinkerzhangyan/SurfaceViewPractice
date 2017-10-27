package tk.thinkerzhangyan.surfaceviewpractice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
*@author zhangyan
*@date 2017/10/26
*/
public class SimpleDraw extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    //正在绘图标志位
    private boolean mIsDrawing;
    //清屏标志位
    private boolean mIsClearScreen;
    //第一次执行的标志位，不设置的话，刚开View是黑色的，因为canvas没进行绘制。
    private boolean mIsFirst;
    private Path mPath;
    private Paint mPaint;

    public SimpleDraw(Context context) {
        super(context);
        initView();
    }

    public SimpleDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SimpleDraw(Context context, AttributeSet attrs,
                      int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        mIsFirst=true;
        new Thread(this).start();
        Log.d("zhangyan","surfaceCreated");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
        Log.d("zhangyan","surfaceDestroy");
    }

    @Override
    public void run() {

        while (mIsDrawing) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            // 50 - 100
            if (end - start < 100) {
                try {
                    Thread.sleep(100 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("zhangyan", "draw");
            if (mIsFirst) {
                mIsDrawing = false;
                mIsFirst = false;
            }

        }

    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            //清除canvas的操作
            while (mIsClearScreen) {
                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);//主要靠这句代码，清除canvas之前绘制的内容。
                mPath.close();
                mPath = new Path();
                mIsClearScreen = false;
                mIsDrawing=false;
            }
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                mIsDrawing = true;//当手指按下的时候，才开启线程进行绘制
                new Thread(this).start();
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mIsDrawing = false;//手指离开屏幕，终止绘制线程
                break;
        }
        return true;
    }


    public void setClearScreen(boolean clearScreen) {
        mIsClearScreen = clearScreen;
        mIsDrawing = true;
        new Thread(this).start();
    }
}
