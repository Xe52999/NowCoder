package com.xe.mynowcoder.util;

//封装拼接redisKey的工具类
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    //关注的实体
    private static final String PREFIX_FOLLOW = "follow";
    //粉丝
    private static final String PREFIX_FOLLOWER = "follower";
    //验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";
    private static final String PREFIX_POST = "post";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)  谁给实体点了赞，就把他存在set集合里
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体
    // userId:follow:entityType -> zset(entityId,now)  now当前时间
    public static String getFollowKey(int userId, int entityType) {
        return userId + SPLIT +PREFIX_FOLLOW  + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // entityType:entityId:follower -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return  entityType + SPLIT + entityId + SPLIT + PREFIX_FOLLOWER;
    }

    // 登录验证码 （，验证码应该是属于某一个用户，每个用户不一样的） owner，用户的一个临时凭证
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }

    // 帖子分数
    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }

}
