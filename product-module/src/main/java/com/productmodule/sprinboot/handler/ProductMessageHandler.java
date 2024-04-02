package com.productmodule.sprinboot.handler;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.productmodule.domain.idempotency.IdempotencyService;
import com.productmodule.sprinboot.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageHandler {

	private final IdempotencyService idempotencyService;
	private final ProductFacade productFacade;


	@RabbitListener(queues = "stock.queue", concurrency = "5")
	public void receiveMessage(AdjustStockCommand command, @Header("Idempotency-Key") String idempotencyKey) throws InterruptedException {

		log.info("receiveMessage {}", command);
		var idempotency = idempotencyService.get(idempotencyKey, String.class);
		if (idempotency.isPresent()) {
			return;
		}

		productFacade.adjustStock(command);

		idempotencyService.put(idempotencyKey, "");
	}
}
