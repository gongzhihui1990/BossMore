package org.gong.bmw.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.gtr.framework.util.Loger;

import org.gong.bmw.R;
import org.gong.bmw.control.BootController;
import org.gong.bmw.control.GameController;
import org.gong.bmw.control.GameItemInterface;
import org.gong.bmw.model.GameItemBitmapView;
import org.gong.bmw.model.GameItemDrawView;
import org.gong.bmw.model.GameItemView;
import org.gong.bmw.model.GamePoint;
import org.gong.bmw.model.GameState;
import org.gong.bmw.model.sea.BaseBoot;
import org.gong.bmw.model.sea.GameTimer;
import org.gong.bmw.model.sea.MainBoot;
import org.gong.bmw.model.sea.Moon;
import org.gong.bmw.model.sea.ScoreBoard;
import org.gong.bmw.model.sea.SubBomb;
import org.gong.bmw.model.sea.Sun;
import org.gong.bmw.model.sea.WaterBomb;
import org.gong.bmw.model.sea.enemy.EnemyBaseBoot;
import org.gong.bmw.model.sea.enemy.EnemyEnum;
import org.gong.bmw.model.sea.enemy.U26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author caroline
 * @date 2018/6/27
 */

@SuppressWarnings("AlibabaMethodTooLong")
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
     * 云画笔
     */
    private Paint pCloud;
    /**
     * 太阳画笔
     */
    private Paint pSun;
    private Sun sun;
    private Moon moon;
    private ScoreBoard scoreBoard;
    private GameTimer gameTimer = GameTimer.Companion.getInstance();
    private int maxEnemySize = 5;

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
        pCloud = new Paint();
        pCloud.setColor(Color.WHITE);
        pCloud.setAntiAlias(true);
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
        final BootController playController =
                new BootController() {
                    @Override
                    public boolean joyButton(Code code) {
                        // 防止list遍历时对list操作
                        synchronized (mSurfaceHolder) {
                            switch (code) {
                                case ReleaseBomb:
                                    if (mainBoot.getGameItemState().getState() == MainBoot.State.normal) {
                                        if (scoreBoard.useBoom()) {
                                            WaterBomb bomb = new WaterBomb();
                                            bomb.releaseAt(mainBoot.getPosition());
                                            mainBoot.joyButton(BootController.Code.ReleaseBomb);
                                            gameItems.add(bomb);
                                        } else {
                                            //TODO 没炸弹了
                                            // onGameOver();
                                        }
                                    }
                                    break;
                                case ClearEnemy:
                                    for (GameItemView gameItemView : gameItems) {
                                        if (gameItemView instanceof EnemyBaseBoot) {
                                            ((EnemyBaseBoot) gameItemView).onCheatDamage();
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
                                    mainBoot.joyButton(code);
                                    break;
                            }
                        }
                        return true;
                    }

                    @Override
                    public void joyStick(int angle, int strength) {
                        mainBoot.joyStick(angle, strength);
                    }
                };

        mGameController.onPlayerControllerPrepared(playController);
        gameItems.add(mainBoot);

        new GameThread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
        gameItems.clear();
    }

    @Override
    public void run() {
        while (mIsRunning) {
            /**取得更新之前的时间**/
            long startTime = System.currentTimeMillis();
            /**在这里加上线程安全锁**/
            synchronized (mSurfaceHolder) {
                doGameDraw();
                doGameMove();
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

    /**
     * 绘制游戏
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private void doGameDraw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            //绘制游戏--开始
            mCanvasWith = mCanvas.getWidth();
            mCanvasHigh = mCanvas.getHeight();
            if (sun == null) {
                sun = new Sun(mCanvasWith, mCanvasHigh);
            }
            if (moon == null) {
                moon = new Moon(mCanvasWith, mCanvasHigh);
            }
            if (scoreBoard == null) {
                scoreBoard = new ScoreBoard(mCanvasWith, mCanvasHigh);
            }
            //绘制：远-近
            if (GameTimer.Companion.getInstance().changed()) {
                //时间变化。消耗食物
                if (!scoreBoard.useFood()) {
                    onGameOver();
                }
            }
            //背景
            mCanvas.drawColor(Color.WHITE);
            switch (GameTimer.Companion.getInstance().getTimeType()) {
                case Night:
                    pSky.setColor(getResources().getColor(R.color.colorBlack));
                    break;
                case MidNoon:
                    pSky.setColor(getResources().getColor(R.color.colorSecondaryBlue));
                    break;
                case Morning:
                    pSky.setColor(getResources().getColor(R.color.colorPrimarySky));
                    break;
                case AfterNoon:
                    pSky.setColor(getResources().getColor(R.color.colorSecondaryGold));
                    break;
                default:
                    break;
            }
            //天空
            Rect rSky = new Rect(0, 0, mCanvasWith, (int) (mCanvasHigh * 0.3));
            mCanvas.drawRect(rSky, pSky);
            //绘制分数板
            scoreBoard.draw(mCanvas, getContext(), GameTimer.Companion.getInstance().getTimeType());
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
                if (view instanceof GameItemBitmapView) {
                    GameItemBitmapView bitmapView = (GameItemBitmapView) view;
                    Bitmap bootBit = bitmapView.getBitmap();
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
                    if (bitmapView instanceof WaterBomb) {
                        WaterBomb waterBomb = (WaterBomb) bitmapView;
                        float rotation = waterBomb.rotation();
                        drawRotateBitmap(mCanvas, bootBit, rotation, viewLeft, viewTop);
                    } else {
                        mCanvas.drawBitmap(bootBit, bootSrcRect, bootDesRect, null);
                    }

                }
                if (view instanceof GameItemDrawView) {
                    GameItemDrawView drawView = (GameItemDrawView) view;
                    drawView.draw(mCanvas, pCloud);
                }
            }//绘制游戏--结束
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawRotateBitmap(Canvas canvas, Bitmap bitmap,
                                  float rotation, float posX, float posY) {
        Matrix matrix = new Matrix();
        int offsetX = bitmap.getWidth() / 2;
        int offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
        canvas.drawBitmap(bitmap, matrix, null);
    }

    private void doGameMove() {
        switch (mGameController.getGameState()) {
            case GameOver:
            case Run:
                //时间流转
                GameTimer.Companion.getInstance().passBy();
                int currentEnemySize = 0;
                for (GameItemView gameItemView : gameItems) {
                    if (gameItemView instanceof EnemyBaseBoot) {
                        currentEnemySize++;
                    }
                }
                //新增敌人----
                if (currentEnemySize < maxEnemySize) {
                    double randomSeed = Math.random();
                    List<EnemyEnum> list = Arrays.asList(EnemyEnum.values());
                    //打乱列表
                    Collections.shuffle(list);
                    for (EnemyEnum enemy : list) {
                        //判断是否可以新增
                        if (enemy.canCreate(randomSeed, scoreBoard)) {
                            gameItems.add(enemy.create(getContext()));
                            break;
                        }
                    }
                }
                //新增敌人----
                //元素移动
                for (GameItemView itemView : gameItems) {
                    if (itemView instanceof MainBoot) {
                        if (mGameController.getGameState() == GameState.GameOver) {
                            //游戏结束
                            continue;
                        }
                    }
                    itemView.move();
                }
                //碰撞处理
                List<EnemyBaseBoot> enemyBoots = new ArrayList<>();
                List<WaterBomb> bombs = new ArrayList<>();
                List<SubBomb> subBombs = new ArrayList<>();
                MainBoot mainBoot = null;
                for (GameItemView itemView : gameItems) {
                    if (itemView instanceof MainBoot) {
                        mainBoot = (MainBoot) itemView;
                        if (BaseBoot.Direct.Stay != ((MainBoot) itemView).getDirect()) {
                            if (!scoreBoard.useOil()) {
                                onGameOver();
                            }
                        }
                        continue;
                    }
                    if (itemView instanceof EnemyBaseBoot) {
                        EnemyBaseBoot enemy = (EnemyBaseBoot) itemView;
                        if (EnemyBaseBoot.State.readyAttack == enemy.getGameItemState().getState()) {
                            Loger.INSTANCE.i("attacking!");
                            MainBoot finalMainBoot = mainBoot;
                            enemy.setAttackCallBack(() -> {
                                if (finalMainBoot != null) {
                                    SubBomb subBomb = new SubBomb();
                                    subBomb.releaseAt(enemy.getPosition(), finalMainBoot.getPosition());
                                    subBombs.add(subBomb);
                                }
                                enemy.setAttackCallBack(null);
                            });
                        }
                        if (EnemyBaseBoot.State.attacking == enemy.getGameItemState().getState()) {
                            Loger.INSTANCE.i("attacking!");
                            enemy.attack();
                        }

                        enemyBoots.add((EnemyBaseBoot) itemView);
                        continue;
                    }
                    if (itemView instanceof WaterBomb) {
                        bombs.add((WaterBomb) itemView);
                        continue;
                    }
                    Loger.INSTANCE.i(itemView.getClass().getName());
                }
                if (!subBombs.isEmpty()) {
                    //添加射向我的水雷
                    gameItems.addAll(subBombs);
                }
                if (bombs.isEmpty() && !scoreBoard.hasBoom()) {
                    onGameOver();
                }
                for (EnemyBaseBoot enemyBoot : enemyBoots) {
                    for (WaterBomb bomb : bombs) {
                        //发生碰撞爆炸
                        if (WaterBomb.State.Run == bomb.getGameItemState().getState()
                                && crashed(enemyBoot, bomb)) {
                            int damage = bomb.bomb();
                            if (EnemyBaseBoot.State.broken != enemyBoot.getGameItemState().getState()) {
                                boolean destroy = enemyBoot.onDamage(damage);
                                if (destroy) {
                                    scoreBoard.addToScore(enemyBoot);
                                }
                            }
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
    }


    private void onGameOver() {
        mGameController.gameOver(scoreBoard);
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


    private boolean crashed(EnemyBaseBoot enemyBoot, WaterBomb bomb) {
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
