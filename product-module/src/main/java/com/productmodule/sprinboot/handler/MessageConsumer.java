package com.productmodule.sprinboot.handler;

import com.commonmodule.dto.product.AdjustStockCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    @RabbitListener(queues = "stock.queue", concurrency = "5")
    public void receiveMessage(AdjustStockCommand adjustStockCommand, @Header("Idempotency-Key") String idempotencyKey) throws InterruptedException {
        // 여기에서 메시지 처리 로직을 구현합니다.
        // 예제에서는 메시지의 내용과 Idempotency-Key 헤더 값을 출력합니다.
        log.info("Received message with Idempotency-Key: " + idempotencyKey);
        log.info("Message content: " + adjustStockCommand.toString());
        Thread.sleep(2000L);
    }
}
