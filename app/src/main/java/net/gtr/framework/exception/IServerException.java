package net.gtr.framework.exception;

/**
 *
 * @author caroline
 * @date 2018/5/7
 */

public class IServerException extends IException {

    private String errorCode;
    public IServerException(String msg ) {
        super(msg);
    }
}
