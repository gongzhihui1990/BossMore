package org.gong.bmw.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;
import org.gong.bmw.control.GameController;
import org.gong.bmw.model.Boot;
import org.gong.bmw.model.EnemyBoot;
import org.gong.bmw.model.EnemyBootCallBack;
import org.gong.bmw.model.MainBoot;
import org.gong.bmw.model.U26;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author caroline
 * @date 2018/6/27
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * 每30帧刷新一次屏幕
     **/
    public static final int TIME_IN_FRAME = 30;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private GameController controller;
    private boolean mIsRunning = false;
    private Path mPath;
    private List<Boot> boots = new ArrayList<>();

    public GameView(Context context, @NonNull GameController controller) {
        super(context);
        Loger.INSTANCE.i("GameView");
        this.controller = controller;
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Loger.INSTANCE.i("surfaceCreated");
        mSurfaceHolder = holder;
        mPath = new Path();
        mIsRunning = true;
        MainBoot mainBoot = new MainBoot(getContext());
        final EnemyBootCallBack enemyBootCallBack = new EnemyBootCallBack() {
            @Override
            public void fade(EnemyBoot boot) {
                boot.onRemove();
            }

            @Override
            public void boom(EnemyBoot boot) {
                boot.onRemove();
            }
        };
        final BootController proxyController = new BootController() {
            @Override
            public void receiveCode(Code code) {
                synchronized (mSurfaceHolder) {
                    switch (code.getScope()) {
                        case All:
                            break;
                        case MainBoot:
                            mainBoot.receiveCode(code);
                        case EnemyBoot:
                            switch (code) {
                                case ClearEnemy:
                                    for (Boot boot : boots) {
                                        if (boot instanceof EnemyBoot) {
                                            enemyBootCallBack.boom((EnemyBoot) boot);
                                        }
                                    }
                                    break;
                                case NewEnemy:
                                    boots.add(new U26(enemyBootCallBack, getContext()));
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void move() {
                mainBoot.move();
            }
        };
        controller.onBootControllerPrepared(proxyController);
        boots.add(mainBoot);

        new GameThread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (mIsRunning) {

            /**取得更新之前的时间**/
            long startTime = System.currentTimeMillis();

            /**在这里加上线程安全锁**/
            synchronized (mSurfaceHolder) {
                switch (controller.getGameState()) {
                    case Run:
                        moveBoots();
                        break;
                    case Pause:
                        //TODO
                        break;
                    default:
                        break;
                }
                try {
                    mCanvas = mSurfaceHolder.lockCanvas();
                    drawing();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            }

            /**取得更新结束的时间**/
            long endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            int diffTime = (int) (endTime - startTime);

            /**确保每次更新时间为30帧**/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void drawing() {
        //SurfaceView背景
        mCanvas.drawColor(Color.WHITE);
        drawBoots();
    }

    private void moveBoots() {
        for (Boot boot : boots) {
            boot.move();
        }
        //删除无效的Boot
        Iterator<Boot> it = boots.iterator();
        while (it.hasNext()) {
            Boot x = it.next();
            if (x.shouldRemove()) {
                it.remove();
            }
        }
    }

    private void drawBoots() {
        for (Boot boot : boots) {
            int with = mCanvas.getWidth();
            int height = mCanvas.getHeight();

            Bitmap bootBit = boot.getBitmap();
            int bootLeft = (int) (with * boot.getPositionHorizon());
            int bootTop = (int) (height * boot.getPositionVertical());
            int bootBitWidth = bootBit.getWidth();
            int bootBitHeight = bootBit.getHeight();
            Rect bootSrcRect = new Rect(0, 0, bootBitWidth, bootBitHeight);
            Rect bootDesRect = new Rect(bootLeft, bootTop, bootLeft + bootBitWidth, bootTop + bootBitHeight);
            mCanvas.drawBitmap(bootBit, bootSrcRect, bootDesRect, null);
        }
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(wSpecSize, hSpecSize);

//        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(300, 300);
//        } else if (wSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(300, hSpecSize);
//        } else if (hSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(wSpecSize, 300);
//        }
    }

}
