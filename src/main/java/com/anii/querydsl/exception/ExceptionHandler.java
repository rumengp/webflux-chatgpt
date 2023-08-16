package com.anii.querydsl.exception;

import com.anii.querydsl.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@Component
public class ExceptionHandler extends AbstractErrorWebExceptionHandler implements Ordered {

    public ExceptionHandler(ErrorAttributes errorAttributes,
                            WebProperties webProperties,
                            ObjectProvider<ViewResolver> viewResolvers,
                            ServerCodecConfigurer serverCodecConfigurer,
                            ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setViewResolvers(viewResolvers.orderedStream().toList());
        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    /**
     * 按照异常类型路由到具体的异常处理方法
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions
                .route(isException(ValidException.class::isInstance), this::handleValidException)
                .andRoute(isException(NotFoundException.class::isInstance), this::handleNotFoundException)
                .andRoute(isException(BusinessException.class::isInstance), this::handleBusinessException)
                .andRoute(isException(Exception.class::isInstance), this::handleException);
    }

    private Mono<ServerResponse> handleNotFoundException(ServerRequest request) {
        if (getError(request) instanceof NotFoundException e) {
            return CommonResult.notFound(e.getMessage());
        }

        return Mono.empty();
    }


    private Mono<ServerResponse> handleBusinessException(ServerRequest request) {
        BusinessException error = (BusinessException) getError(request);
        return CommonResult.error(error);
    }

    private Mono<ServerResponse> handleException(ServerRequest request) {
        Exception e = (Exception) getError(request);
        log.error(e.getMessage(), e);
        return CommonResult.error();
    }

    public Mono<ServerResponse> handleValidException(ServerRequest request) {
        Throwable error = getError(request);
        return CommonResult.bad(error.getMessage());
    }

    /**
     * 判断抛出的异常是否是指定的类型, 函数柯里化
     * isException(err -> err instanceof Exception)
     */
    private RequestPredicate isException(Predicate<Throwable> function) {
        return request -> {
            Throwable error = getError(request);
            return function.test(error);
        };
    }

    @Override
    public int getOrder() {
        return -2; // order需要小于默认的全局处理器
    }
}

