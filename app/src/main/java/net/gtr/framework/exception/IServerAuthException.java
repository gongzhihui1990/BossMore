package net.gtr.framework.exception;

/**
 * Created by caroline on 2018/5/7.
 */

public class IServerAuthException extends IServerException {

    public IServerAuthException() {
        super("需要重新登录");
    }


}
