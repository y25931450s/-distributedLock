package com.xiaoying.base.lock.exception;

/**
 * @author gaofang
 */
public class DistributedLockErrorMessage implements ErrorMessage {
    public static final DistributedLockErrorMessage OK = new DistributedLockErrorMessage("[base-common-distributedLock] ok");
    public static final DistributedLockErrorMessage ZK_DELETE_ERROR = new DistributedLockErrorMessage("[base-common-distributedLock] zookeeper unlock delete error !");
    public static final DistributedLockErrorMessage GET_APOLLO_FAIL = new DistributedLockErrorMessage("[base-common-distributedLock]  apollo get fail ！");
    public static final DistributedLockErrorMessage LOCK_TYPE_NOT_LOCK = new DistributedLockErrorMessage("[base-common-distributedLock] Can't get lockType corresponding lock !");
    public static final DistributedLockErrorMessage ANNOTATION_KEY_IS_NULL = new DistributedLockErrorMessage("[base-common-distributedLock] annotation lock key can not be NULL! ");
    public static final DistributedLockErrorMessage CLASS_KEY_IS_NULL = new DistributedLockErrorMessage("[base-common-distributedLock]  lock key can not be NULL! ");
    public static final DistributedLockErrorMessage REDIS_LOCK_FAIL = new DistributedLockErrorMessage("[base-common-distributedLock]  redis lock fail! ");
    public static final DistributedLockErrorMessage REDIS_UNLOCK_FAIL = new DistributedLockErrorMessage("[base-common-distributedLock]  redis unlock fail! ");
    public static final DistributedLockErrorMessage ZK_LOCK_FAIL = new DistributedLockErrorMessage("[base-common-distributedLock]  zookeeper lock fail! ");
    public static final DistributedLockErrorMessage ZK_UNLOCK_FAIL = new DistributedLockErrorMessage("[base-common-distributedLock]  zookeeper unlock fail! ");


    private String message;

    protected DistributedLockErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
