package org.gong.bmw.model;

import android.content.Context;

import org.gong.bmw.control.BootController;
import org.gong.bmw.control.GameItemInterface;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface GameBoot extends BootController {
    /**
     * 初始化
     */
    void initBoot(Context context);


}
