package com.productmodule.domain.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

	private final IdempotencyRepository idempotencyRepository;
	private final ObjectMapper objectMapper;

	public <T> Optional<T> get(final String idempotencyKey, final Class<T> clazz) {

		if (!StringUtils.hasText(idempotencyKey)) {
			return Optional.empty();
		}

		return idempotencyRepository.findById(idempotencyKey)
				.map(key -> {
					try {
						return Optional.of(objectMapper.readValue(key.getResponse(), clazz));
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				})
				.orElse(Optional.empty());

	}

	public void put(final String idempotencyKey, final Object response) {
		String responseJson = null;
		try {
			responseJson = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		var saveIdempotencyKey = Idempotency.of(idempotencyKey, responseJson);
		idempotencyRepository.save(saveIdempotencyKey);
	}


}
