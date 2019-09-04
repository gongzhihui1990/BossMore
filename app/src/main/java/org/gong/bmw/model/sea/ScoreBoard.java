package org.gong.bmw.model.sea;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.gong.bmw.App;
import org.gong.bmw.R;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model.sea
 * @date:2019-09-04
 */
public class ScoreBoard {
    protected int maxW;
    protected int maxH;
    private Rect imageOilDesRect;
    private Rect imageBoomDesRect;
    private Rect imageFoodDesRect;
    private int score = 0;
    private int boom = 10;
    private float oil = 10;
    private int food = 10;
    private Paint textPaint;
    private Bitmap imageOil, imageBomb, imageFood;

    public ScoreBoard(int mCanvasWith, int mCanvasHigh) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        maxW = (int) (mCanvasWith * 0.2);
        maxH = (int) (mCanvasHigh * 0.3);

        imageOil = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.oil);

        imageBomb = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.bomb);

        imageFood = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.food);


    }

    public void addScore(int value) {
        score += value;
    }

    public void addFood() {
        food++;
    }

    public boolean useFood() {
        if (food <= 0) {
            return false;
        }
        food--;
        return true;
    }

    public void addBoom() {
        boom++;
    }

    public boolean useBoom() {
        if (boom <= 0) {
            return false;
        }
        boom--;
        return true;
    }

    public void addOil() {
        boom++;
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
