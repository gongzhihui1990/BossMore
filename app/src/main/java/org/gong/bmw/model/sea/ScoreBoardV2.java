package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.gong.bmw.App;
import org.gong.bmw.R;
import org.gong.bmw.model.sea.enemy.EnemyBaseBoot;
import org.gong.bmw.model.sea.enemy.EnemySupply;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model.sea
 * @date:2019-09-04 记分板
 */
public class ScoreBoardV2 {
    protected int maxW;
    protected int maxH;
    private int heart = 5;
    private Rect imageOilDesRect;
    private Rect imageBoomDesRect;
    private Rect imageFoodDesRect;
    private int score = 0;
    private int boom = 100;
    private float oil = 100;
    private int food = 100;
    private Paint textPaint;
    private Bitmap imageOil, imageBomb, imageFood, imageHeart;

    public ScoreBoardV2(int mCanvasWith, int mCanvasHigh) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        maxW = (int) (mCanvasWith * 0.2);
        maxH = (int) (mCanvasHigh * 0.3);

        imageHeart = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.heart_full);

        imageOil = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.oil);

        imageBomb = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.bomb);

        imageFood = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.food);


    }

    public void loseHeart(int damage) {
        heart -= damage;
    }

    public int getHeart() {
        return heart;
    }

    public int getScore() {
        return score;
    }


    public void addToScore(EnemyBaseBoot enemyBoot) {
        EnemySupply supply = enemyBoot.getSupply();
        score += supply.getScore();
        boom += supply.getBoom();
        food += supply.getFood();
        oil += supply.getOil();
    }


    public boolean useFood() {
        if (food <= 0) {
            return false;
        }
        food--;
        return true;
    }


    public boolean hasBoom() {
        return boom > 0;
    }

    public boolean useBoom() {
        if (boom <= 0) {
            return false;
        }
        boom--;
        return true;
    }


    public boolean useOil() {
        if (((int) oil) <= 0) {
            return false;
        }
        oil -= 0.01;
        return true;
    }

    public void draw(Canvas canvas, Context context, GameTimeType timeType) {
        switch (timeType) {
            case Night:
                textPaint.setColor(context.getResources().getColor(R.color.colorWhite));
                break;
            case MidNoon:
                textPaint.setColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case Morning:
                textPaint.setColor(context.getResources().getColor(R.color.colorBlack));
                break;
            case AfterNoon:
                textPaint.setColor(context.getResources().getColor(R.color.colorBlack));
                break;
            default:
                break;
        }
        //heart
        for (int hp = heart; hp > 0; hp--) {
            Rect imageHeartRect = new Rect(
                    canvas.getWidth() - (imageHeart.getWidth() + 8) * (hp + 1),
                    32,
                    canvas.getWidth() - (imageHeart.getWidth() + 8) * hp,
                    imageHeart.getHeight() + 32);
            canvas.drawBitmap(imageHeart, null, imageHeartRect, null);
        }
        //计分
        textPaint.setTextSize(32);
        canvas.drawText("sc", 32, 1 * maxH / 5, textPaint);
        canvas.drawText(": " + score, 72, 1 * maxH / 5, textPaint);

        //弹药
        if (imageBoomDesRect == null) {
            imageBoomDesRect = new Rect(32, (int) (1.5 * maxH / 5), imageBomb.getWidth() + 32, imageBomb.getHeight() + (int) (1.5 * maxH / 5));
        }
        canvas.drawBitmap(imageBomb, null, imageBoomDesRect, null);
        canvas.drawText(": " + boom, 72, 2 * maxH / 5, textPaint);

        //燃油
        if (imageOilDesRect == null) {
            imageOilDesRect = new Rect(32, (int) (2.5 * maxH / 5), imageOil.getWidth() + 32, imageOil.getHeight() + (int) (2.5 * maxH / 5));
        }
        canvas.drawBitmap(imageOil, null, imageOilDesRect, null);
        canvas.drawText(": " + ((int) oil), 72, 3 * maxH / 5, textPaint);

        //食物
        if (imageFoodDesRect == null) {
            imageFoodDesRect = new Rect(32, (int) (3.5 * maxH / 5), imageFood.getWidth() + 32, imageFood.getHeight() + (int) (3.5 * maxH / 5));
        }
        canvas.drawBitmap(imageFood, null, imageFoodDesRect, null);
        canvas.drawText(": " + food, 72, 4 * maxH / 5, textPaint);


    }
}
