package com.hywx.siin.common;

/**
 * @program: user-serice
 * @description: 前端传递信息封装
 * @author: tangjing
 * @create: 2019-09-25 09:27
 **/
public class Resp {
	/** 用于状态码 */
    public static final int SUCCESS = 200;
    public static final int ERROR = 404;
    public static final int TOKENERROR=1001;

    /** 数据类型 */
    public static final String LIST = "LIST";
    public static final String SINGLE = "SINGLE";
    public static final String MAP = "MAP";
    public static final String PAGE = "PAGE";
    public static final String STRING = "STRING";
    public static final String JSONSTRING = "JSONSTRING";

    /** 操作简单提示 */
    public static final String SUCCESS_INFO = "操作成功";
    public static final String ERROR_INFO = "操作失败";
    /** 状态码 */
    private int code;
    /** 返回提示信息 */
    private String message;
    /** 数据类型 */
    private String dataType;
    /** 返回对象 */
    private Object respBody;

    private Resp() {}

    /**
     * @Description:  返回实例化Resp
     * @return:  Resp
     */
    public static Resp getInstantiation() {
        return new Resp();
    }

    /**
     * @Description:  返回实例化Resp
     * @Param:  @param code 状态码
     * @Param:  @param message 返回提示信息
     * @Param:  @param dataType 数据类型
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiation(int code,String message,String dataType,Object respBody) {
        Resp resp = getInstantiation();
        resp.setCode(code);
        resp.setMessage(message);
        resp.setDataType(dataType);
        resp.setRespBody(respBody);
        return resp;
    }

    /**
     *
     * @Description:  返回状态为Success的实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param dataType 数据类型
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccess(String message,String dataType,Object respBody) {
        return getInstantiation(SUCCESS, message, dataType, respBody);
    }
    public static Resp getInstantiationTokenError(String message,String dataType,Object respBody) {
        return getInstantiation(TOKENERROR, message, dataType, respBody);
    }
    /**
     * @Description:  返回状态为Success的String类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccessString(String message,Object respBody) {
        return getInstantiation(SUCCESS, message, STRING, respBody);
    }
    /**
     *
     * @author:  zy
     * @Description:  返回状态为Success的List类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccessList(String message,Object respBody) {
        return getInstantiation(SUCCESS, message, LIST, respBody);
    }

    /**
     * @Description:  返回状态为Success的Map类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccessMap(String message,Object respBody) {
        return getInstantiation(SUCCESS, message, MAP, respBody);
    }
    /**
     * @Description:  返回状态为Success的Page类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccessPage(String message,Object respBody) {
        return getInstantiation(SUCCESS, message, PAGE, respBody);
    }


    /**
     * @Description:  返回状态为Success的Page类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationSuccessJsonString(String message,Object respBody) {
        return getInstantiation(SUCCESS, message, JSONSTRING, respBody);
    }


    /**
     * @Description:  返回状态为Error的实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param dataType 数据类型
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationError(String message,String dataType,Object respBody) {
        return getInstantiation(ERROR, message, dataType, respBody);
    }
    /**
     * @Description:  返回状态为Error的String类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationErrorString(String message,Object respBody) {
        return getInstantiation(ERROR, message, STRING, respBody);
    }
    /**
     * @Description:  返回状态为Error的List类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationErrorList(String message,Object respBody) {
        return getInstantiation(ERROR, message, LIST, respBody);
    }
    /**
     * @Description:  返回状态为Error的Map类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationErrorMap(String message,Object respBody) {
        return getInstantiation(ERROR, message, MAP, respBody);
    }
    /**
     * @Description:  返回状态为Error的Page类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationErrorPage(String message,Object respBody) {
        return getInstantiation(ERROR, message, PAGE, respBody);
    }
    /**
     * @Description:  返回状态为Error的JsonString类型实例化Resp
     * @Param:  @param message 返回提示信息
     * @Param:  @param respBody 返回对象
     * @return:  Resp
     */
    public static Resp getInstantiationErrorJsonString(String message,Object respBody) {
        return getInstantiation(ERROR, message, JSONSTRING, respBody);
    }


    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Object getRespBody() {
        return respBody;
    }
    public void setRespBody(Object respBody) {
        this.respBody = respBody;
    }

    /**
     * @Description:  判断是否成功
     * @return:  boolean true:成功，false:错误
     */
    public boolean isSuccess() {
        return this.code==SUCCESS;
    }
}
