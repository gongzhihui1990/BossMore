package org.gong.bmw.control;

import org.gong.bmw.model.GameState;
import org.gong.bmw.model.sea.ScoreBoard;

/**
 * @author caroline
 * @date 2018/6/27
 */

public interface GameController {
    GameState getGameState();

    void onPlayerControllerPrepared(BootController bootController);

    void gameOver(ScoreBoard scoreBoard);
}
