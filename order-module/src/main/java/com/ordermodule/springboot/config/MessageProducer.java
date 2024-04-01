package com.ordermodule.springboot.config;

import com.commonmodule.dto.product.AdjustStockCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(UUID uuid, AdjustStockCommand adjustStockCommand) {
        MessageConverter converter = rabbitTemplate.getMessageConverter();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("Idempotency-Key", uuid.toString());
        Message message = converter.toMessage(adjustStockCommand, messageProperties);

        rabbitTemplate.send("your.exchange.name", "your.routing.key", message);
    }
}
