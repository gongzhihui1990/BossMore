package org.gong.bmw.model.sea.enemy;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model.sea.enemy
 * @date:2019-09-05
 */
public class EnemySupply {
    private int score;
    private int boom;
    private float oil;
    private int food;

    private EnemySupply(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public float getOil() {
        return oil;
    }

    public int getBoom() {
        return boom;
    }

    public int getFood() {
        return food;
    }

    public static class Builder {
        private EnemySupply supply;

        public static Builder create(int value) {
            Builder supplyBuilder = new Builder();
            supplyBuilder.supply = new EnemySupply(value);
            return supplyBuilder;
        }


        Builder setBoom(int value) {
            supply.boom = value;
            return this;
        }

        Builder setOil(float value) {
            supply.oil = value;
            return this;
        }

        Builder setFood(int value) {
            supply.food = value;
            return this;
        }

        EnemySupply build() {
            return supply;
        }
    }
}