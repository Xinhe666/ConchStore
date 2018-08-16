package com.apple.conchstore.live.net;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/8 下午2:39
 * - @Email whynightcode@gmail.com
 */
public interface Api {

    String HOST = "http://api.anwenqianbao.com/v2/";


    String ANWEI="http://api.anwenqianbao.com/v2/borrow/url";
    /**
     * banner
     **/
    String BANNER = HOST + "vest/banner";

    String NEWS = HOST + "vest/news";

    String RECOMMEND = HOST + "vest/recommendProduct";

    String LIKE = HOST + "vest/likeProduct";
    /**
     * 热门
     */
    String HOT = HOST + "vest/hotProduct";
    /**
     * 产品
     **/
    String PRODUCT_LSIT = HOST + "vest/product";

    /**
     * 筛选
     */
    String PRODUCT_SCREEN = HOST + "vest/screening";
    /**
     * 福利
     **/
    String WELFARE = HOST + "vest/welfare";
    /**
     * 统计
     */
    String APPLY = HOST + "vest/apply";

    /**
     * 新or老用户
     **/
    String isOldUser = HOST + "quick/isOldUser";
    /**
     * 验证码获取
     **/
    String CODE = HOST + "sms/getcode";
    /**
     * 验证码效验
     **/
    String CHECKCODE = HOST + "sms/checkCode";

    /**
     * 状态
     **/
    String getStatus = HOST + "vest/getStatus";
    /**
     * 版本更新
     **/
    String UPDATE = HOST + "vest/version";
}

