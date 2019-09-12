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
import org.gong.bmw.model.sea.SubBullet;
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
     * 每20毫秒刷新一帧屏幕
     **/
    public static final int TIME_IN_FRAME = 20;
    private static int mCanvasWith;
    private static int mCanvasHigh;
    /**
     * 游戏画板
     */
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private GameController mGameController;
    private boolean mIsRunning = false;
    private List<GameItemView> gameItems = new ArrayList<>();
    private List<GameItemView> newGameItems = new ArrayList<>();
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
    private int maxEnemySize = 10;

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

    public static int getHigh() {
        return mCanvasHigh;
    }

    public static int getWith() {
        return mCanvasWith;
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
                                            GamePoint mp = mainBoot.getPosition();
                                            float rx = mp.getX() + (float) mainBoot.getBitmap().getWidth() / (mCanvasWith * 2);
                                            float ry = mp.getY() + (float) mainBoot.getBitmap().getHeight() / (mCanvasHigh * 2);
                                            GamePoint releasePoint = new GamePoint(rx, ry);
                                            bomb.releaseAt(releasePoint);
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

            /**确保每次更新时间为50帧**/
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
                    int viewLeft = (int) (mCanvasWith * view.getPosition().getX());
                    int viewTop = (int) (mCanvasHigh * view.getPosition().getY());
                    int viewBitWidth = bootBit.getWidth();
                    int viewBitHeight = bootBit.getHeight();
                    Rect bootSrcRect = new Rect(0, 0, viewBitWidth, viewBitHeight);
                    Rect bootDesRect = new Rect(viewLeft, viewTop, viewLeft + viewBitWidth, viewTop + viewBitHeight);
                    if (bitmapView instanceof WaterBomb) {
                        WaterBomb waterBomb = (WaterBomb) bitmapView;
                        float rotation = waterBomb.rotation();
                        //带旋转效果
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
                //新增敌人---- 平均3000毫秒新增一个,当前50HZ=20毫秒
                //新增概率1/150
                if (System.currentTimeMillis() % 100 == 0) {
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
                List<WaterBomb> waterBombs = new ArrayList<>();
                List<SubBullet> subBombs = new ArrayList<>();
                MainBoot mainBoot = null;
                for (GameItemView itemView : gameItems) {
                    if (itemView instanceof EnemyBaseBoot) {
                        EnemyBaseBoot enemy = (EnemyBaseBoot) itemView;
                        if (EnemyBaseBoot.State.readyAttack == enemy.getGameItemState().getState()) {
                            if (enemy instanceof U26) {
                                Loger.INSTANCE.i("set attacking call!");
                                MainBoot finalMainBoot = mainBoot;
                                enemy.setAttackCallBack(() -> {
                                    if (finalMainBoot != null) {
                                        SubBullet subBomb = new SubBullet();
                                        subBomb.releaseAt(enemy, finalMainBoot);
                                        newGameItems.add(subBomb);
                                    }
                                    enemy.setAttackCallBack(null);
                                });
                            }
                        }
                        if (EnemyBaseBoot.State.attacking == enemy.getGameItemState().getState()) {
                            Loger.INSTANCE.i("attacking!");
                            enemy.attack();
                        }

                        enemyBoots.add((EnemyBaseBoot) itemView);
                        continue;
                    }
                    if (itemView instanceof WaterBomb) {
                        waterBombs.add((WaterBomb) itemView);
                        continue;
                    }
                    if (itemView instanceof SubBullet) {
                        subBombs.add((SubBullet) itemView);
                        continue;
                    }
                    if (itemView instanceof MainBoot) {
                        mainBoot = (MainBoot) itemView;
                        if (BaseBoot.Direct.Stay != ((MainBoot) itemView).getDirect()) {
                            if (!scoreBoard.useOil()) {
                                onGameOver();
                            }
                        }
                        continue;
                    }
                    Loger.INSTANCE.i(itemView.getClass().getName());
                }

                if (mainBoot != null) {
                    for (SubBullet bullet : subBombs) {
                        //是否攻击到船体
                        if (SubBullet.State.Run == bullet.getGameItemState().getState()
                                && crashedMain(mainBoot, bullet)) {
                            int damage = bullet.bomb();
                            scoreBoard.loseHeart(damage);
                            if (scoreBoard.getHeart() == 0) {
                                onGameOver();
                            }
                            break;
                        }
                    }

                }
                if (!newGameItems.isEmpty()) {
                    //添加射向我的水雷
                    gameItems.addAll(newGameItems);
                    newGameItems.clear();
                }
                if (waterBombs.isEmpty() && !scoreBoard.hasBoom()) {
                    onGameOver();
                }
                for (EnemyBaseBoot enemyBoot : enemyBoots) {
                    for (WaterBomb bomb : waterBombs) {
                        //攻击到敌舰，发生碰撞爆炸
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
                Loger.INSTANCE.w("gameItems size:" + gameItems.size());
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

    private boolean crashed(EnemyBaseBoot boot, WaterBomb bomb) {
//        2. 矩形的碰撞检测方法1
//        碰撞条件：
//        x抽距离差 < 两矩形宽度之和 / 2
//        y抽距离差 < 两矩形高度之和 / 2
        final int with = mCanvasWith;
        final int height = mCanvasHigh;

        GamePoint p1 = boot.getPosition();
        GamePoint p2 = bomb.getPosition();

        int w12 = boot.getBitmap().getWidth() + bomb.getBitmap().getWidth();
        float dxf = (p1.getX() * with + (float) boot.getBitmap().getWidth() / 2) - (p2.getX() * with + (float) bomb.getBitmap().getWidth() / 2);
        float dx = Math.abs(dxf);
        if (dx > w12 / 2) {
            return false;
        }

        int h12 = boot.getBitmap().getHeight() + bomb.getBitmap().getHeight();
        float dyf = (p1.getY() * height + (float) boot.getBitmap().getHeight() / 2) - (p2.getY() * height + (float) bomb.getBitmap().getHeight() / 2);
        float dy = Math.abs(dyf);
        if (dy > h12 / 2) {
            return false;
        }
        return true;
    }

    private boolean crashedMain(GameItemBitmapView boot, GameItemBitmapView bomb) {
//        2. 矩形的碰撞检测方法1
//        碰撞条件：
//        x抽距离差 < 两矩形宽度之和 / 2
//        y抽距离差 < 两矩形高度之和 / 2
        final int with = mCanvasWith;
        final int height = mCanvasHigh;

        GamePoint p1 = boot.getPosition();
        GamePoint p2 = bomb.getPosition();

        float bootR = (float) Math.sqrt(Math.pow((float) boot.getBitmap().getWidth() / 2, 2) + Math.pow(boot.getBitmap().getHeight() / 2, 2));
        //p1中心坐标
        float p1x = p1.getX() * with + (float) boot.getBitmap().getWidth() / 2;
        float p1y = p1.getY() * height + (float) boot.getBitmap().getHeight() / 2;
        //p2中心坐标
        float p2x = p2.getX() * with + (float) bomb.getBitmap().getWidth() / 2;
        float p2y = p2.getY() * height + (float) bomb.getBitmap().getHeight() / 2;
        //两中心距离
        float dx = p1x - p2x;
        float dy = p1y - p2y;

        float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        if (distance > bootR / 2) {
            return false;
        }
        return true;
    }

}
