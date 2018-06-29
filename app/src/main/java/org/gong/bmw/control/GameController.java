package org.gong.bmw.control;

import org.gong.bmw.model.GameState;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface GameController   {
    GameState getGameState();

    void onBootControllerPrepared(BootController bootController);

}
