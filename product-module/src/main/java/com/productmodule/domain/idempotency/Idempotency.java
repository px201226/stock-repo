package com.productmodule.domain.idempotency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Idempotency {

	@Id
	private String idempotencyKey;

	private String response;

	private LocalDateTime createAt;


	public static Idempotency of(final String idempotencyKey, final String response){
		return new Idempotency(idempotencyKey, response, LocalDateTime.now());
	}
}
