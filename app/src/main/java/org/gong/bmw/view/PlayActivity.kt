package org.gong.bmw.view

import android.os.Bundle
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_play.*
import net.gtr.framework.rx.ProgressObserverImplementation
import net.gtr.framework.rx.RxHelper
import net.gtr.framework.util.ToastUtil
import org.gong.bmw.R
import org.gong.bmw.control.BootController
import org.gong.bmw.control.GameController
import org.gong.bmw.game.GameResource
import org.gong.bmw.game.SeaFightGameView
import org.gong.bmw.model.GameState
import org.gong.bmw.model.sea.ScoreBoard

/**
 *
 * @author caroline
 * @date 2018/6/27
 */

class PlayActivity : BaseActivity() {
    var mainGameController: MainGameController? = null
    override val layoutR: Int
        get() = R.layout.activity_play

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainGameController = MainGameController()
        RxHelper.bindOnUI(Observable.just(true).map { GameResource.init(getContext()) }, object : ProgressObserverImplementation<Boolean>() {
            override fun onNext(t: Boolean) {
                super.onNext(t)
                rootView.addView(SeaFightGameView(this@PlayActivity, mainGameController!!))
            }
        }.setMessage("loading"))

    }


    override fun onBackPressed() {
        this.mainGameController?.onBackPressed()
    }

    inner class MainGameController : GameController {
        lateinit var bootController: BootController
        fun onBackPressed() {
            bootController.joyButton(BootController.Code.GameMenu)
        }

        override fun onPlayerControllerPrepared(bootController: BootController) {
            this.bootController = bootController
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
            if (state == GameState.GameOver) {
                return
            }
            state = GameState.GameOver
            //TODO GameOver Score
            RxHelper.bindOnUI(Observable.just("GameOver Score"),
                    object : ProgressObserverImplementation<String>(this@PlayActivity) {
                        override fun onNext(t: String) {
                            super.onNext(t)
                            ToastUtil.show(t)
                        }
                    })
        }

        override fun getGameState(): GameState {
            return state
        }

    }
}
