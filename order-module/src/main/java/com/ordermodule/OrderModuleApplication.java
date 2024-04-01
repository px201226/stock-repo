package com.ordermodule;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import com.ordermodule.domain.order.ProductClient;
import com.ordermodule.springboot.config.MessageProducer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication

public class OrderModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderModuleApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(MessageProducer messageProducer) {
		return (args) -> {
			messageProducer.sendMessage(UUID.randomUUID(), new AdjustStockCommand(Map.of(1L,2L)));
			messageProducer.sendMessage(UUID.randomUUID(), new AdjustStockCommand(Map.of(2L,2L)));
			messageProducer.sendMessage(UUID.randomUUID(), new AdjustStockCommand(Map.of(3L,2L)));
			messageProducer.sendMessage(UUID.randomUUID(), new AdjustStockCommand(Map.of(4L,2L)));
		};
	}


}
