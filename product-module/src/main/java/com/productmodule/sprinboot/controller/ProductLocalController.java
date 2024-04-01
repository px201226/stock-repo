package com.productmodule.sprinboot.controller;

import com.commonmodule.dto.product.AdjustStockCommand;
import com.commonmodule.dto.product.DecreaseStockCommand;
import com.commonmodule.dto.product.ProductViewModel;
import com.productmodule.domain.product.ProductCommand.ProductCreateCommand;
import com.productmodule.domain.product.ProductCommand.ProductUpdateCommand;
import com.productmodule.sprinboot.facade.ProductFacade;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/local")
public class ProductLocalController {

	private final ProductFacade productFacade;

	@GetMapping("/getProducts/{productIds}")
	public ResponseEntity<List<ProductViewModel>> getProducts(@PathVariable Set<Long> productIds) {
		var productViewModel = productFacade.getProducts(productIds);
		return ResponseEntity.ok(productViewModel);
	}

	@PutMapping("/adjustStock")
	public ResponseEntity<Void> adjustStock(@RequestBody AdjustStockCommand command){
		productFacade.adjustStock(command);
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}

}