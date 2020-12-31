package com.hywx.sisl.redis;

/*
 * REDIS KEY 命名规则
 */
public class RedisFind {
	/**
     * 分系统前缀标识
     */
    public static final String KEY_PREFIX = "sisl";
    /**
     * 分割字符，默认[:]，使用:可用于rdm分组查看
     */
    private static final String KEY_SPLIT_CHAR = ":";
    
    /**
     * redis的key键规则定义
     * @param subsystem
     * @param item
     * @param ids
     * @return key
     */
    public static String keyBuilder(String subsystem, String item, String... ids) {
        return keyBuilder(null, subsystem, item, ids);
    }
    
    public static String keyBuilder(String subsystem, String item, String id) {
        return keyBuilder(null, subsystem, item, new String[]{id});
    }

    /**
     * redis的key键规则定义
     * @param prefix
     * @param subsystem
     * @param item
     * @param id
     * @return key
     */
    public static String keyBuilder(String prefix, String subsystem, String item, String id) {
        return keyBuilder(prefix, subsystem, item, new String[]{id});
    }

    /**
     * redis的key键规则定义
     * @param prefix
     * @param subsystem
     * @param item
     * @param ids
     * @return key
     */
    public static String keyBuilder(String prefix, String subsystem, String item, String... ids) {
        // 项目前缀
        if (prefix == null) {
            prefix = KEY_PREFIX;
        }
        StringBuilder key = new StringBuilder(prefix);
        // KEY_SPLIT_CHAR 为分割字符
        key.append(KEY_SPLIT_CHAR).append(subsystem).append(KEY_SPLIT_CHAR).append(item);
        for (String id : ids) {
            key.append(KEY_SPLIT_CHAR).append(id);
        }
        return key.toString();
    }

    /**
     * redis的key键规则定义
     * @param redisEnum
     * @param id
     * @return key
     */
    public static String keyBuilder(RedisEnum redisEnum, String id) {
        return keyBuilder(redisEnum.getPrefix(), redisEnum.getSubsystem(), redisEnum.getItem(), id);
    }

}
