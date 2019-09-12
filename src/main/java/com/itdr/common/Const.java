package com.itdr.common;

public class Const {
    //用户相关状态
    public static final Integer USER_PARAMETER_CODE = 101;
    public static final String USER_PARAMETER_MSG = "参数为空！";

    public static final Integer USER_NO_CODE = 103;
    public static final String USER_NO_MSG = "用户不存在！";

    public static final Integer USER_DISABLE_CODE = 104;
    public static final String USER_DISABLE_MSG = "用户已禁用！";

    //商品相关状态
    public static final Integer PRODUCT_HAVE_CODE = 107;
    public static final String PRODUCT_HAVE_MSG = "您要添加的商品已存在，并更新成功！";
    public static final Integer PRODUCT_SEARCH_CODE = 109;
    public static final String PRODUCT_SEARCH_MSG = "您要查询的商品不存在！";


    //商品分类相关状态
    public static final Integer CATEGORY_NO_CODE = 105;
    public static final String CATEGORY_NO_MSG = "您要查看的分类不存在！";
    public static final Integer CATEGORY_HAVE_CODE = 106;
    public static final String CATEGORY_HAVE_MSG = "您要添加的分类已存在！";
    //订单相关状态
}
