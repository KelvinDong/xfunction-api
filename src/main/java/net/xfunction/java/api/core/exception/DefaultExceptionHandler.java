package net.xfunction.java.api.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;
import net.xfunction.java.api.core.utils.Result;
import net.xfunction.java.api.core.utils.ResultCodeEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类,用于向前端抛出信息，如果不需要向前端抛的，建议方法内部catch住就行。
 * @author Kelvin
 *
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseBody
    @ExceptionHandler({Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleThrowable(HttpServletRequest request, Throwable e) {
        log.error("execute methond exception error.url is {}", request.getRequestURI(), e);
        return Result.failure(e, ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }


    
    /**
     * 服务异常
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({BizException.class})
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Result handleBizException(HttpServletRequest request, BizException e) {
        log.error("execute methond [BIZ] exception error.url is {}", request.getRequestURI()+" "+e.getCode()+" "+e.getMsg());        
        return Result.failure(e);
    }

    /**
     * 参数校验异常
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(HttpServletRequest request, BindException e, BindingResult br) {
        log.error("execute methond exception error.url is {}", request.getRequestURI(), e);
        return Result.failure(br);
    }

}
