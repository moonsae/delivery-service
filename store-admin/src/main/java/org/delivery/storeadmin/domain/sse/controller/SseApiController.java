package org.delivery.storeadmin.domain.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sse")
public class SseApiController {

    private final SseConnectionPool sseConnectionPool;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter connect(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserSession userSession
    ){
        log.info("login user {}", userSession);

        /*var emitter = new SseEmitter(1000L * 60); //ms
        userConnection.put(userSession.getUserId().toString(), emitter);

        var temp = UserSseConnection.connect(
                userSession.getUserId().toString(),
                sseConnectionPool);
        emitter.onTimeout(()->{
            log.info("on timeout");
            //클라이언트와 타임아웃이 일어났을 때
            emitter.complete();
        });
        emitter.onCompletion(()->{
            log.info("completion");
            //클라이언트와 연결이 종료되었을 때 하는 작업
            userConnection.remove(userSession.getUserId().toString());
        });
        //최초 연결시 응답 전송
        var event = SseEmitter
                .event()
                .name("onopen")
                .data("connect")
                ;
        try {
            emitter.send(event);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;*/
        var userSseConnection = UserSseConnection.connect(
                userSession.getStoreId().toString(),
                sseConnectionPool,
                objectMapper
        );
        //session에 추가
        sseConnectionPool.addSession(userSseConnection.getUniqueKey(), userSseConnection);
        return userSseConnection.getSseEmitter();
    }

    @GetMapping("/push-event")
    public void pushEvent(
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserSession userSession
    ){/*
        //기존에 연결된 유저 찾기
        var emitter = userConnection.get(userSession.getUserId().toString());
        var event = SseEmitter
                .event()
                .data("hello") //onmessage
                ;
        try {
            emitter.send(event);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }*/
        var userSseConnection = sseConnectionPool.getSession(userSession.getStoreId().toString());
        Optional.ofNullable(userSseConnection)
                .ifPresent(it->{
                    it.sendMessage("hello world");
                });
    }
}
