package org.gong.bmw.view

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_play.*
import org.gong.bmw.R
import org.gong.bmw.control.BootController
import org.gong.bmw.control.GameController
import org.gong.bmw.game.GameUserCallBack
import org.gong.bmw.game.SeaFightGameView
import org.gong.bmw.model.GameState

/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class PlayActivity : BaseActivity(), GameUserCallBack {
    var oil = 100
    var food = 100
    var bomb = 100
    private fun initSource() {
        oil = 100
        food = 100
        bomb = 100
        renderSource()
    }

    private fun renderSource() {
        this@PlayActivity.tvOil.text = "" + oil
        this@PlayActivity.tvFood.text = "" + food
        this@PlayActivity.tvBomb.text = "" + bomb
    }

    override fun onAddOil(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUseBomb(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddBomb(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUseFood(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddFood(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUseOil(size: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val layoutR: Int
        get() = R.layout.activity_play

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView.addView(SeaFightGameView(this, MainGameController()))
        initSource()
    }

    inner class MainGameController : GameController {

        override fun onPlayerControllerPrepared(bootController: BootController) {
            this@PlayActivity.btnSwitch.setOnClickListener { start = !start }
            this@PlayActivity.btnAdd.setOnClickListener {
                bootController.receiveCode(BootController.Code.NewEnemy)
            }
            this@PlayActivity.btnRelease.setOnClickListener { bootController.receiveCode(BootController.Code.ReleaseBomb) }
            this@PlayActivity.joystickView.setOnMoveListener { angle, strength ->
                run {
                    bootController.joystick(angle, strength)
                }
            }
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
