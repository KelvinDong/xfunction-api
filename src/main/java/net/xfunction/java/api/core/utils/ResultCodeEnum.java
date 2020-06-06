package net.xfunction.java.api.core.utils;


/**
 * @author Kelvin
 */
public enum ResultCodeEnum {
    /**
     *
     */
	//系统 与http.status保持一致，主要用于exception
    OK("200", "OK"),
    BAD_REQUEST("400", "bad request"),
    INTERNAL_SERVER_ERROR("500", "internal server error"),
    NOT_IMPLEMENTED("501", "not implemented"),
    
    //以下自定义
    FAILD("4400","Operation failed"),
    
    UNAUTHORIZED("4000", "未授权"),
    UNAUTHORIZED_TOKEN_ERROR("4001", "unauthorized token error or expired"),    
    
    USER_EXISTS("4100","用户已经存在"),
    USER_MAIL_UNCONFIRMED("4101","邮箱未确认"),
    USER_BLOCKED("4102","用户锁定"),
    USER_NOT_EXISTS("4103","用户不存在"),
    USER_WRONG_AUTH("4104","密码错误"),
    
    SEND_MAIL_FAIL("4200","发送邮件失败"),
    SEND_MAIL_MORE("4201","send mail more"),
    MAIL_BINDED("4202","邮箱已经被绑定"),
    
    
    PARAMS_MISS("4300", "参数缺失"),
    PARAM_ERROR("4301", "参数错误"),    
    MORE_REQUEST("4302","请求过多"),
    URL_NOT_REACHED("4303","URL不能访问");
		
	
    
    private String code;
    private String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
