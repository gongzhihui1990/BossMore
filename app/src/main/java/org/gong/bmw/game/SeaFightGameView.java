package org.gong.bmw.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.gtr.framework.util.Loger;

import org.gong.bmw.control.BootController;
import org.gong.bmw.control.GameController;
import org.gong.bmw.control.GameItemInterface;
import org.gong.bmw.model.GameItemView;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.sea.EnemyBoot;
import org.gong.bmw.model.sea.Fish;
import org.gong.bmw.model.sea.GameTimer;
import org.gong.bmw.model.sea.MainBoot;
import org.gong.bmw.model.sea.Moon;
import org.gong.bmw.model.sea.Sun;
import org.gong.bmw.model.sea.U21;
import org.gong.bmw.model.sea.U26;
import org.gong.bmw.model.sea.WaterBomb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author caroline
 * @date 2018/6/27
 */

@SuppressLint("ViewConstructor")
public class SeaFightGameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**
     * 每30帧刷新一次屏幕
     **/
    public static final int TIME_IN_FRAME = 30;
    /**
     * 游戏画板
     */
    private Canvas mCanvas;
    private int mCanvasWith;
    private int mCanvasHigh;
    private SurfaceHolder mSurfaceHolder;
    private GameController mGameController;
    private boolean mIsRunning = false;
    private List<GameItemView> gameItems = new ArrayList<>();
    /**
     * 海洋画笔
     */
    private Paint pSea;
    /**
     * 天空画笔
     */
    private Paint pSky;
    /**
     * 太阳画笔
     */
    private Paint pSun;

    private Sun sun;
    private Moon moon;

    private GameTimer gameTimer = GameTimer.Companion.getInstance();

    public SeaFightGameView(Context context, @NonNull GameController controller) {
        super(context);
        Loger.INSTANCE.i("GameView");
        this.mGameController = controller;
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        pSea = new Paint();
        pSea.setColor(Color.BLUE);
        pSea.setAntiAlias(true);
        pSky = new Paint();
        pSky.setColor(Color.WHITE);
        pSky.setAntiAlias(true);
        pSun = new Paint();
        pSun.setAntiAlias(true);
        pSun.setColor(Color.RED);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Loger.INSTANCE.i("surfaceCreated");
        mSurfaceHolder = holder;
        mIsRunning = true;
        MainBoot mainBoot = new MainBoot(getContext());
        final BootController proxyController = code -> {
            synchronized (mSurfaceHolder) {
                switch (code) {
                    case ReleaseBomb:
                        if (mainBoot.getGameItemState().getState() == MainBoot.State.normal) {
                            WaterBomb bomb = new WaterBomb();
                            bomb.releaseAt(mainBoot.getPosition());
                            mainBoot.receiveCode(BootController.Code.ReleaseBomb);
                            gameItems.add(bomb);
                        }

                        break;
                    case ClearEnemy:
                        for (GameItemView gameItemView : gameItems) {
                            if (gameItemView instanceof EnemyBoot) {
                                ((EnemyBoot) gameItemView).fade();
                            }
                        }
                        break;
                    case NewEnemy:
                        if (gameItems.size() > 8) {
                            return false;
                        }
                        gameItems.add(new U26(getContext()));
                        break;

                    default:
                        mainBoot.receiveCode(code);
                        break;
                }
            }
            return true;
        };
        mGameController.onPlayerControllerPrepared(proxyController);
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
                switch (mGameController.getGameState()) {
                    case Run:
                        //时间流转
                        GameTimer.Companion.getInstance().passBy();
                        //新增敌人
                        int enemyBootSize = 0;
                        for (GameItemView gameItemView : gameItems) {
                            if (gameItemView instanceof EnemyBoot) {
                                enemyBootSize++;
                            }
                        }
                        double random = Math.random();
                        Loger.INSTANCE.d("random: " + random);
                        if (enemyBootSize < 10 && random * 100 < 3) {
                            gameItems.add(new U26(getContext()));
                        }
                        if (enemyBootSize < 10 && random * 100 < 15 && random * 100 > 10) {
                            gameItems.add(new U21(getContext()));
                        }
                        if (enemyBootSize < 10 && random * 100 == 16) {
                            gameItems.add(new Fish(getContext()));
                        }
                        //元素移动
                        for (GameItemView itemView : gameItems) {
                            itemView.move();
                        }
                        //碰撞处理
                        List<EnemyBoot> enemyBoots = new ArrayList<>();
                        List<WaterBomb> bombs = new ArrayList<>();
                        for (GameItemView itemView : gameItems) {
                            if (itemView instanceof EnemyBoot) {
                                enemyBoots.add((EnemyBoot) itemView);
                            }
                            if (itemView instanceof WaterBomb) {
                                bombs.add((WaterBomb) itemView);
                            }
                        }
                        for (EnemyBoot enemyBoot : enemyBoots) {
                            for (WaterBomb bomb : bombs) {
                                if (crashed(enemyBoot, bomb)) {
                                    bomb.bomb();
                                    enemyBoot.bomb();
                                }
                            }
                        }
                        //删除无效的Boot
                        Iterator<GameItemView> it = gameItems.iterator();
                        while (it.hasNext()) {
                            GameItemInterface x = it.next();
                            if (x.shouldRemove()) {
                                it.remove();
                            }
                        }
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
        mCanvasWith = mCanvas.getWidth();
        mCanvasHigh = mCanvas.getHeight();
        if (sun == null) {
            sun = new Sun(mCanvasWith, mCanvasHigh);
        }
        if (moon == null) {
            moon = new Moon(mCanvasWith, mCanvasHigh);
        }

        //绘制：远-近

        //背景
        mCanvas.drawColor(Color.WHITE);
        //天空
        Rect rSky = new Rect(0, 0, mCanvasWith, (int) (mCanvasHigh * 0.3));
        mCanvas.drawRect(rSky, pSky);
        //太阳
        mCanvas.drawCircle(sun.getCx(), sun.getCy(), sun.getCr(), pSun);
        //月亮
        int moonLeft = (int) (moon.getCx());
        int moonTop = (int) (moon.getCy());
        Bitmap moonBit = moon.getBitmap();
        int moonBitWidth = moonBit.getWidth();
        int moonBitHeight = moonBit.getHeight();
        Rect moonSrcRect = new Rect(0, 0, moonBitWidth, moonBitHeight);
        Rect moonDesRect = new Rect(moonLeft, moonTop, moonLeft + moonBitWidth, moonTop + moonBitHeight);
        mCanvas.drawBitmap(moonBit, moonSrcRect, moonDesRect, null);
        //大海
        Rect rSea = new Rect(0, (int) (mCanvasHigh * 0.3), mCanvasWith, mCanvasHigh);
        mCanvas.drawRect(rSea, pSea);
        //游戏其他元素
        for (GameItemView view : gameItems) {
            Bitmap bootBit = view.getBitmap();
            int highBuff = 0;
            if (view instanceof MainBoot) {
                highBuff = (int) (bootBit.getHeight() * 0.8);
            }
            int viewLeft = (int) (mCanvasWith * view.getPosition().getX());
            int viewTop = (int) (mCanvasHigh * view.getPosition().getY()) - highBuff;
            int viewBitWidth = bootBit.getWidth();
            int viewBitHeight = bootBit.getHeight();
            Rect bootSrcRect = new Rect(0, 0, viewBitWidth, viewBitHeight);
            Rect bootDesRect = new Rect(viewLeft, viewTop, viewLeft + viewBitWidth, viewTop + viewBitHeight);
            mCanvas.drawBitmap(bootBit, bootSrcRect, bootDesRect, null);
        }
    }

    private boolean crashed(EnemyBoot enemyBoot, WaterBomb bomb) {
//        2. 矩形的碰撞检测方法1
//        碰撞条件：
//        x抽距离差 < 两矩形宽度之和 / 2
//        y抽距离差 < 两矩形高度之和 / 2
        final int with = mCanvasWith;
        final int height = mCanvasHigh;

        GamePoint p1 = enemyBoot.getPosition();
        GamePoint p2 = bomb.getPosition();

        int w12 = enemyBoot.getBitmap().getWidth() + bomb.getBitmap().getWidth();
        float dxf = (p1.getX() * with + enemyBoot.getBitmap().getWidth() / 2) - (p2.getX() * with + bomb.getBitmap().getWidth() / 2);
        float dx = Math.abs(dxf);
        if (dx > w12 / 2) {
            return false;
        }

        int h12 = enemyBoot.getBitmap().getHeight() + bomb.getBitmap().getHeight();
        float dyf = (p1.getY() * with + enemyBoot.getBitmap().getHeight() / 2) - (p2.getY() * with + bomb.getBitmap().getHeight() / 2);
        float dy = Math.abs(dyf);
        if (dy > h12 / 2) {
            return false;
        }
        return true;
    }


}
