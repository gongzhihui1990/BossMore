package org.gong.bmw.control;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface BootController {
    /**
     * 发送控制码
     *
     * @param code
     * @return 是否成功执行
     */
    boolean receiveCode(Code code);

    void joystick(int angle, int strength);


    enum Code {
        NewEnemy, ClearEnemy, Left, Right, Stop, Sink, ReleaseBomb;
    }

    enum Scope {
        MainBoot, EnemyBoot, All,
    }
}
