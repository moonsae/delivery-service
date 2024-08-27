package org.delivery.api.exceptionhandler;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.api.Api;
import org.delivery.api.common.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* *
 * 우리가 캐치하지 못한 예외가 일어난 경우
 * */
@Slf4j
@RestControllerAdvice //예외를 다 이곳으로 모을꺼임
@Order(value = Integer.MAX_VALUE) //가장 마지막에 실행 적용 default라고 설정해도 됨
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception(
            Exception exception
    ){
        log.error("",exception);
        return ResponseEntity
                .status(500)
                .body(
                    Api.ERROR(ErrorCode.SERVER_ERROR)
                );
    }
}
