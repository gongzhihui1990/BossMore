package org.gong.bmw.control;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface BootController /*extends  GameItemInterface*/{
    /**
     * 发送控制码
     *
     * @param code
     * @return 是否成功执行
     */
    boolean receiveCode(Code code);


    enum Code {
        NewEnemy,ClearEnemy, Left, Right, Stop,Sink,ReleaseBomb;


//        @NonNull
//        public Scope getScope() {
//            switch (this) {
//                case NewEnemy:
//                    return Scope.EnemyBoot;
//                case ClearEnemy:
//                    return Scope.EnemyBoot;
//                case Left:
//                    return Scope.MainBoot;
//                case Right:
//                    return Scope.MainBoot;
//                case Stop:
//                    return Scope.MainBoot;
//                default:
//                    return Scope.All;
//            }
//        }
    }

    enum Scope {
        MainBoot, EnemyBoot, All,

    }
}
