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
public class ScoreBoard {
    protected int maxW;
    protected int maxH;
    private int heart = 5;
    private int score = 0;
    private Paint textPaint;
    private Bitmap  imageHeart;

    public ScoreBoard(int mCanvasWith, int mCanvasHigh) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        maxW = (int) (mCanvasWith * 0.2);
        maxH = (int) (mCanvasHigh * 0.3);

        imageHeart = BitmapFactory.decodeResource(App.Companion.getInstance().getResources(),
                R.mipmap.heart_full);


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
    }


    public boolean useFood() {
        return true;
    }


    public boolean hasBoom() {
        return true;
    }

    public boolean useBoom() {
        return true;
    }


    public boolean useOil() {
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
        canvas.drawText("Score", 32, 1 * maxH / 5, textPaint);
        canvas.drawText(": " + score, 120, 1 * maxH / 5, textPaint);


    }
}
