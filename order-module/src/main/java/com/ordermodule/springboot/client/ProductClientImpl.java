package com.ordermodule.springboot.client;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import com.ordermodule.domain.exception.NetworkException;
import com.ordermodule.domain.order.ProductClient;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

	@Value("${client.product-service.host}")
	private String PRODUCT_SERVICE_HOST;

	@Value("${client.product-service.port}")
	private String PRODUCT_SERVICE_PORT;

	private final RestTemplate restTemplate;


	@Override
	public List<ProductViewModel> findAllById(Set<Long> productIds) {

		var uri = UriComponentsBuilder.newInstance()
				.scheme("http")
				.host(PRODUCT_SERVICE_HOST)
				.port(PRODUCT_SERVICE_PORT)
				.path("/local/getProducts/{productIds}")
				.buildAndExpand(Map.of("productIds", String.join(",", productIds.stream().map(String::valueOf).toList())))
				.toUri();

		try {
			var exchange = restTemplate.exchange(
					uri,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<ProductViewModel>>() {
					}
			);

			return exchange.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new NetworkException(e);
		}
	}


	@Override public void adjustStock(UUID uuid, AdjustStockCommand command) {
		var uri = UriComponentsBuilder.newInstance()
				.scheme("http")
				.host(PRODUCT_SERVICE_HOST)
				.port(PRODUCT_SERVICE_PORT)
				.path("/local/adjustStock")
				.build()
				.toUri();

		var requestEntity = RequestEntity
				.put(uri)
				.header("Idempotency-Key", uuid.toString())
				.body(command);

		try {
			restTemplate.exchange(requestEntity, String.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new NetworkException(e);
		}
	}
}
