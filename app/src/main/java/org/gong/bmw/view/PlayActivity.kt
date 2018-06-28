package org.gong.bmw.view

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_play.*
import org.gong.bmw.R
import org.gong.bmw.control.BootController
import org.gong.bmw.control.GameController
import org.gong.bmw.game.GameView
import org.gong.bmw.model.GameState

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
        rootView.addView(GameView(this, MainGameController()))
    }

    inner class MainGameController : GameController {

        override fun onBootControllerPrepared(bootController: BootController) {
            this@PlayActivity.btnLeft.setOnClickListener({ bootController.receiveCode(BootController.Code.Left) })
            this@PlayActivity.btnRight.setOnClickListener({ bootController.receiveCode(BootController.Code.Right) })
            this@PlayActivity.btnSwitch.setOnClickListener({ start = !start })
            this@PlayActivity.btnStop.setOnClickListener({ bootController.receiveCode(BootController.Code.Stop) })
            this@PlayActivity.btnAdd.setOnClickListener({ bootController.receiveCode(BootController.Code.NewEnemy) })
        }

        var start: Boolean = true

        override fun getGameState(): GameState {
            if (start) {
                return GameState.Run
            }
            return GameState.Pause
        }

    }
}
