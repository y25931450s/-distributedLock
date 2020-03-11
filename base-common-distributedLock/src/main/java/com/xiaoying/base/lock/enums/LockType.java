package com.xiaoying.base.lock.enums;

public enum LockType {


    redis(1),

    zk(2);

    private int lockType;

    LockType(int lockType) {
        this.lockType = lockType;
    }



}
