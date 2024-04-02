package com.ordermodule.springboot.client;

import com.commonmodule.dto.product.AdjustStockCommand;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(UUID idempotencyKey, Object adjustStockCommand) {

		MessageConverter converter = rabbitTemplate.getMessageConverter();
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setHeader("Idempotency-Key", idempotencyKey.toString());
		Message message = converter.toMessage(adjustStockCommand, messageProperties);

		rabbitTemplate.send("stock.exchange", "key", message);
	}
}
