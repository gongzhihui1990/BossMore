package org.gong.bmw.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;
import org.gong.bmw.control.GameController;
import org.gong.bmw.control.GameItemInterface;
import org.gong.bmw.model.EnemyBoot;
import org.gong.bmw.model.EnemyBootCallBack;
import org.gong.bmw.model.GameItemView;
import org.gong.bmw.model.MainBoot;
import org.gong.bmw.model.U26;
import org.gong.bmw.model.WaterBomb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author caroline
 * @date 2018/6/27
 */

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * 每30帧刷新一次屏幕
     **/
    public static final int TIME_IN_FRAME = 30;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private GameController controller;
    private boolean mIsRunning = false;
    private List<GameItemInterface> gameItems = new ArrayList<>();

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
            public boolean receiveCode(Code code) {
                synchronized (mSurfaceHolder) {
                    mainBoot.receiveCode(code);
                    switch (code) {
                        case ReleaseBomb:
                            WaterBomb bomb = new WaterBomb();
                            bomb.releaseAt(mainBoot.getPoint());
                            gameItems.add(bomb);
                            break;
                        case ClearEnemy:
                            for (GameItemInterface controller : gameItems) {
                                if (controller instanceof EnemyBoot) {
                                    enemyBootCallBack.boom((EnemyBoot) controller);
                                }
                            }
                            break;
                        case NewEnemy:
                            if (gameItems.size() > 8) {
                                return false;
                            }
                            gameItems.add(new U26(enemyBootCallBack, getContext()));
                            break;

                        default:
                            break;
                    }
                }
                return true;
            }

        };
        controller.onBootControllerPrepared(proxyController);
        gameItems.add(mainBoot);

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
                        moveItems();
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
                break;
            case MotionEvent.ACTION_MOVE:
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
        drawItems();
    }

    private void moveItems() {
        for (GameItemInterface controller : gameItems) {
            controller.move();
        }
        //删除无效的Boot
        Iterator<GameItemInterface> it = gameItems.iterator();
        while (it.hasNext()) {
            GameItemInterface x = it.next();
            if (x.shouldRemove()) {
                it.remove();
            }
        }
    }

    private void drawItems() {
        for (GameItemInterface gameItem : gameItems) {

            if (gameItem instanceof GameItemView) {
                int with = mCanvas.getWidth();
                int height = mCanvas.getHeight();
                GameItemView view = ((GameItemView) gameItem);
                Bitmap bootBit = view.getBitmap();
                int viewLeft = (int) (with * view.getPoint().getX());
                int viewTop = (int) (height * view.getPoint().getY());
                int viewBitWidth = bootBit.getWidth();
                int viewBitHeight = bootBit.getHeight();
                Rect bootSrcRect = new Rect(0, 0, viewBitWidth, viewBitHeight);
                Rect bootDesRect = new Rect(viewLeft, viewTop, viewLeft + viewBitWidth, viewTop + viewBitHeight);
                mCanvas.drawBitmap(bootBit, bootSrcRect, bootDesRect, null);
            }


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
