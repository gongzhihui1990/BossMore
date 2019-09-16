package org.gong.bmw.control;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface BootController {
    /**
     * 接受手柄按键输入
     *
     * @param code
     * @return 是否成功执行
     */
    boolean joyButton(Code code);

    /**
     * 接受手柄摇杆输入
     * @param angle
     * @param strength
     */
    void joyStick(int angle, int strength);


    enum Code {
        NewEnemy, ClearEnemy, Left, Right, Stop, Sink, ReleaseBomb,GameMenu
    }

}
