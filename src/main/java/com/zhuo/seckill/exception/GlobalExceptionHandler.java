package com.zhuo.seckill.exception;


import com.zhuo.seckill.vo.RespBean;
import com.zhuo.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice = @ControllerAdvice + @ResponseBody
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e){
         // 处理全局异常
         if(e instanceof GlobalException){
             GlobalException ge = (GlobalException) e;
             return RespBean.error(ge.getRespBeanEnum());
         }
         // 处理参数校验异常
         else if(e instanceof BindException){
             BindException be = (BindException) e;
             RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
             respBean.setMessage("参数校验异常: " + be.getBindingResult().getAllErrors().get(0).getDefaultMessage());
             return respBean;
         }
         return RespBean.error(RespBeanEnum.ERROR);
    }

}
