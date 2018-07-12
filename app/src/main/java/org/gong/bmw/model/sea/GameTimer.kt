package org.gong.bmw.model.sea

/**
 * Created by caroline on 2018/7/11.
 */

class GameTimer private constructor() {

    private var now: Long = 0

    fun passBy(len: Int) {
        now += len.toLong()
        now %= DAY_LEN
    }

    fun passBy() {
        passBy(50)
    }

    fun getPercent(): Float {
            return now.toFloat() / DAY_LEN
    }

    fun getTimeType(): GameTimeType {
        return when {
            now < DAY_LEN_6 -> GameTimeType.Morning
            now < DAY_LEN_11 -> GameTimeType.MidNoon
            now < DAY_LEN_18 -> GameTimeType.AfterNoon
            now > DAY_LEN_20 -> GameTimeType.Night
            else -> {
                GameTimeType.Morning
            }
        }
    }

    companion object {
        private val DAY_LEN = (24 * 60 * 60).toLong()
        private val DAY_LEN_6 = (6 * 60 * 60).toLong()
        private val DAY_LEN_11 = (11 * 60 * 60).toLong()
        private val DAY_LEN_18 = (18 * 60 * 60).toLong()
        private val DAY_LEN_20 = (20 * 60 * 60).toLong()

        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = GameTimer()
    }
}
