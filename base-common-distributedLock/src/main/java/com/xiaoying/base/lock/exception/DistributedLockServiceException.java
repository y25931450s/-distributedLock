package com.xiaoying.base.lock.exception;


/**
 * @author gaofang
 */
public class DistributedLockServiceException extends RuntimeException {

    private ErrorMessage errorMessage = DistributedLockErrorMessage.OK;
    private Object data;

    public DistributedLockServiceException(ErrorMessage errorMessage) {
        this(errorMessage, errorMessage.getMessage());
    }

    public DistributedLockServiceException(ErrorMessage errorMessage, String message) {
        this(errorMessage, message, null);
    }

    public DistributedLockServiceException(ErrorMessage errorMessage, String message, Object data) {
        super(message);
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public DistributedLockServiceException(ErrorMessage errorMessage, String message, Object data, Throwable cause) {
        super(message, cause);
        this.errorMessage = errorMessage;
        this.data = data;
    }
    public DistributedLockServiceException(ErrorMessage errorMessage, Throwable cause, Object data) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public Object getData() {
        return data;
    }
}
