package cn.apimix.core.handler;

import cn.apimix.common.enums.HttpStatusEnum;
import cn.apimix.common.resp.Result;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理/error异常
 * @author Hor
 */
@RestController
public class CustomErrorHandle extends AbstractErrorController {
    public static final String ERROR_PATH = "/error";


    public CustomErrorHandle(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(ERROR_PATH)
    public Result<?> handleError(HttpServletRequest request) {
        return Result.buildFail(HttpStatusEnum.NOT_FOUND);
    }
}