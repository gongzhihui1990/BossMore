package org.gong.bmw.game;

/**
 *
 * @author caroline
 * @date 2018/6/28
 */

public class GameThread extends Thread {
    private SeaFightGameView gameView;

    public GameThread(SeaFightGameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void run() {
        gameView.run();
    }
}
