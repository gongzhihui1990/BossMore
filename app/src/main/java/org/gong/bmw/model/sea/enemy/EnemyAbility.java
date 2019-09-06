package org.gong.bmw.model.sea.enemy;

import org.gong.bmw.model.sea.BaseBoot;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model.sea.enemy
 * @date:2019-09-06
 */
public class EnemyAbility {
    private int HP;
    private BaseBoot.Speed speed;

    public int getHP() {
        return HP;
    }


    public BaseBoot.Speed getSpeed() {
        return speed;
    }

    public static class Builder {
        private EnemyAbility ability;

        public static Builder create() {
            Builder supplyBuilder = new Builder();
            supplyBuilder.ability = new EnemyAbility();
            return supplyBuilder;
        }

        public Builder setSpeed(BaseBoot.Speed speed) {
            this.ability.speed = speed;
            return this;
        }

        public Builder setHP(int hp) {
            this.ability.HP = hp;
            return this;
        }

        public EnemyAbility build() {
            return this.ability;
        }

    }
}
