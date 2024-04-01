package com.ordermodule;

import com.commonmodule.dto.product.ProductViewModel;
import com.ordermodule.domain.order.ProductClient;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication

public class OrderModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderModuleApplication.class, args);
	}




}
