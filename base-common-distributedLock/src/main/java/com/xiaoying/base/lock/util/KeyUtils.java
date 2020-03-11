package com.xiaoying.base.lock.util;

import com.xiaoying.base.lock.exception.DistributedLockErrorMessage;
import com.xiaoying.base.lock.exception.DistributedLockServiceException;
import org.springframework.util.StringUtils;

public class KeyUtils {

    public static String getAnnotationKey(String preLock, String args) {
        if (StringUtils.isEmpty(preLock) || StringUtils.isEmpty(args)) {
            throw new DistributedLockServiceException(DistributedLockErrorMessage.ANNOTATION_KEY_IS_NULL);
        }
        return preLock + "&&" + args;
    }

    public static String getClassKey(String preLock) {

        String key = null;
        if (StringUtils.isEmpty(preLock)) {
            throw new DistributedLockServiceException(DistributedLockErrorMessage.CLASS_KEY_IS_NULL);
        }

        key = AppUtils.loadAppName(null) + "&&" + preLock;
        return key;
    }


}
