package com.hywx.sirs.common;

/**
 * @program: user-serice
 * @description: 服务传递信息封装
 * @author: tangjing
 * @create: 2019-09-25 09:10
 **/
public class ContextUtils {
	/** 用于状态码 */
    public static final int SUCCESS = 200;
    public static final int ERROR = 404;

    /** 操作简单提示 */
    public static final String SUCCESS_INFO = "操作成功";
    public static final String ERROR_INFO = "操作失败";

    /** 状态码 */
    private int code;
    /** 返回值说明信息 */
    private String message;
    /** 数据类型 */
    private Class<?> classType;
    /** 返回对象 */
    private Object respBody;

    private ContextUtils() {}
    /**
     *
     * @Description:  获取返回值对象
     * @Param:  @return
     * @return:  ContextUtils
     */
    public static ContextUtils getInstantiation() {
        return new ContextUtils();
    }

    /**
     *
     * @Description:  获取Success状态的返回值对象
     * @Param:  @param message 返回提示信息
     * @Param:  @param classType Class类型
     * @Param:  @param respBody 返回对象
     * @return:  ContextUtils
     */
    public static ContextUtils getInstantiationSuccess(String message,Class<?> classType,Object respBody) {
        ContextUtils instantiation = getInstantiation();
        instantiation.setCode(SUCCESS);
        instantiation.setClassType(classType);
        instantiation.setMessage(message);
        instantiation.setRespBody(respBody);
        return instantiation;
    }
    /**
     *
     * @Description:  获取Success状态的返回值对象
     * @Param:  @param classType Class类型
     * @Param:  @param respBody 返回对象
     * @return:  ContextUtils
     */
    public static ContextUtils getInstantiationSuccess(Class<?> classType,Object respBody) {
        ContextUtils instantiation = getInstantiation();
        instantiation.setCode(SUCCESS);
        instantiation.setClassType(classType);
        instantiation.setMessage(SUCCESS_INFO);
        instantiation.setRespBody(respBody);
        return instantiation;
    }
    /**
     *
     * @Description:  获取Error状态的返回值对象
     * @Param:  @param message 返回提示信息
     * @Param:  @param classType Class类型
     * @Param:  @param respBody 返回对象
     * @return:  ContextUtils
     */
    public static ContextUtils getInstantiationError(String message,Class<?> classType,Object respBody) {
        ContextUtils instantiation = getInstantiation();
        instantiation.setCode(ERROR);
        instantiation.setClassType(classType);
        instantiation.setMessage(message);
        instantiation.setRespBody(respBody);
        return instantiation;
    }
    /**
     *
     * @Description:  获取Error状态的返回值对象
     * @Param:  @param message 返回提示信息
     * @Param:  @param classType Class类型
     * @Param:  @param respBody 返回对象
     * @return:  ContextUtils
     */
    public static ContextUtils getInstantiationError(Class<?> classType,Object respBody) {
        ContextUtils instantiation = getInstantiation();
        instantiation.setCode(ERROR);
        instantiation.setClassType(classType);
        instantiation.setMessage(ERROR_INFO);
        instantiation.setRespBody(respBody);
        return instantiation;
    }

    /**
     *
     * @Description:  判断是否成功
     * @return:  boolean true:成功，false:错误
     */
    public boolean isSuccess() {
        return this.code==SUCCESS;
    }

    /** Get And Set */
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
    public Class<?> getClassType() {
        return classType;
    }
    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }
    public Object getRespBody() {
        return respBody;
    }
    public void setRespBody(Object respBody) {
        this.respBody = respBody;
    }
}
