package org.gong.bmw.model.sea.enemy;

import android.content.Context;
import android.support.annotation.NonNull;

import org.gong.bmw.model.GameItemView;
import org.gong.bmw.model.sea.ScoreBoard;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model.sea
 * @date:2019-09-05
 */
public enum EnemyEnum {

    /**
     *
     */
    Dolphin(0.03f, 0, 0),
    U21(0.01f, 0, 100),
    U26(0.01f, 0, 0),

    U50(0.05f, 50, 0),
    Shark(0.01f, 50, 0),

    U21_PLUS(0.02f, 100, 0);

    private final int minScore;
    private final int maxScore;
    private float p;

    EnemyEnum(float p, int minScore, int maxScore) {
        this.p = p;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }


    /**
     * 生成算法
     *
     * @param random
     * @param score
     * @return 是否能生成该类型的精灵
     */
    public boolean canCreate(double random, @NonNull ScoreBoard score) {
        if (score.getScore() < this.minScore) {
            //分数不够，还不能创建
            return false;
        }
        if (this.maxScore > 0 && score.getScore() > this.maxScore) {
            //分数过高，不再出现
            return false;
        }
        return random <= p;
    }

    /**
     * 创建业务
     *
     * @param context
     * @return
     */
    public GameItemView create(Context context) {
        switch (this) {
            case U21_PLUS:
                return new U21_PLUS(context);
            case U21:
                return new U21(context);
            case U26:
                return new U26(context);
            case U50:
                return new U50(context);
            case Dolphin:
                return new Dolphin(context);
            case Shark:
                return new Shark(context);
            default:
                break;
        }
        return null;
    }

}
