package com.ordermodule.springboot.config;

import com.commonmodule.dto.product.AdjustStockCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "your.queue.name")
    public void receiveMessage(AdjustStockCommand adjustStockCommand, @Header("Idempotency-Key") String idempotencyKey) {
        // 여기에서 메시지 처리 로직을 구현합니다.
        // 예제에서는 메시지의 내용과 Idempotency-Key 헤더 값을 출력합니다.
        System.out.println("Received message with Idempotency-Key: " + idempotencyKey);
        System.out.println("Message content: " + adjustStockCommand.toString());
    }
}
