package com.xiaoying.base.lock.init;

import lombok.Data;

/**
 * @author gaofang
 * @date 2019/8/30 16:23
 */
@Data
public class ApolloData {

    private String redisUrl;

    private String redisPassword;

    private String zkAddress;

    private Integer sessionTimeout;

    private Integer connectionTimeout;
}
