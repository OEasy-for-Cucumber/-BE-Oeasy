package com.OEzoa.OEasy.api.voting;

import com.OEzoa.OEasy.util.timeTrace.TimeTrace;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@TimeTrace
public class StompController {

    @MessageMapping("/send") // 클라이언트에서 "/app/send"로 보내는 메시지를 처리
    @SendTo("/topic/messages") // 모든 구독자에게 "/topic/messages"로 메시지 전송
    public MessageResponse sendMessage(MessageRequest message) {
        return new MessageResponse("Hello, " + HtmlUtils.htmlEscape(message.getContent()) + "!");
    }

}
