package org.gong.bmw.view

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_play.*
import net.gtr.framework.util.ToastUtil
import org.gong.bmw.R
import org.gong.bmw.control.BootController
import org.gong.bmw.control.GameController
import org.gong.bmw.game.SeaFightGameView
import org.gong.bmw.model.GameState
import org.gong.bmw.model.sea.ScoreBoard

/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class PlayActivity : BaseActivity() {

    override val layoutR: Int
        get() = R.layout.activity_play

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView.addView(SeaFightGameView(this, MainGameController()))
    }

    inner class MainGameController : GameController {

        override fun onPlayerControllerPrepared(bootController: BootController) {
            this@PlayActivity.btnSwitch.setOnClickListener {
                start = !start
                if (start) {
                    GameState.Run
                } else {
                    GameState.Pause
                }
            }
            this@PlayActivity.btnAdd.setOnClickListener {
                bootController.joyButton(BootController.Code.NewEnemy)
            }
            this@PlayActivity.btnRelease.setOnClickListener { bootController.joyButton(BootController.Code.ReleaseBomb) }
            this@PlayActivity.joystickView.setOnMoveListener { angle, strength ->
                run {
                    bootController.joyStick(angle, strength)
                }
            }
        }

        var start: Boolean = true

        private var state = GameState.Run
        override fun gameOver(score: ScoreBoard) {
            state = GameState.GameOver
            //TODO
            ToastUtil.show("GameOver Score")
        }

        override fun getGameState(): GameState {
            return state
        }

    }
}
