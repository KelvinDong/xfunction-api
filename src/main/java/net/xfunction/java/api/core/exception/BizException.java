package net.xfunction.java.api.core.exception;


import lombok.Getter;
import lombok.Setter;
import net.xfunction.java.api.core.utils.ResultCodeEnum;


/**
 * Biz 异常处理类
 * @author Kelvin
 *
 */

@Getter
@Setter
public class BizException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8730608017801645010L;

	private String code;

    private String msg;

    public BizException(String msg) {
        super(msg);
    }

    public BizException(ResultCodeEnum resultCodeEnum) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMsg());
    }

    public BizException(ResultCodeEnum resultCodeEnum, String msg) {
        super(msg);
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getCode();
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

   
}
