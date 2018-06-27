package net.gtr.framework.exception

/**
 *
 * @author caroline
 * @date 2018/5/7
 */

open class IServerException(msg: String) : IException(msg) {

    private val errorCode: String? = null
}
