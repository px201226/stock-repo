package com.productmodule.sprinboot.mapper;

import com.commonmodule.dto.product.ProductViewModel;
import com.productmodule.domain.product.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

	 ProductViewModel toProductViewModel(Product product);

	List<ProductViewModel> toProductViewModelList(List<Product> products);
}
