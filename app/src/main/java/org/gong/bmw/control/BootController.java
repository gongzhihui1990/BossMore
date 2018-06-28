package org.gong.bmw.control;

import android.support.annotation.NonNull;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface BootController {
    /**
     * 发送控制码
     *
     * @param code
     */
    void receiveCode(Code code);

    /**
     * 时间pass
     */
    void move();

    enum Code {
        NewEnemy,ClearEnemy, Left, Right, Stop;

        @NonNull
        public Scope getScope() {
            switch (this) {
                case NewEnemy:
                    return Scope.EnemyBoot;
                case ClearEnemy:
                    return Scope.EnemyBoot;
                case Left:
                    return Scope.MainBoot;
                case Right:
                    return Scope.MainBoot;
                case Stop:
                    return Scope.MainBoot;
                default:
                    return Scope.All;
            }
        }
    }

    enum Scope {
        MainBoot, EnemyBoot, All,

    }
}
