package kz.dara.test.qs.mapper;

import kz.dara.test.qs.dto.ProductDTO;
import kz.dara.test.qs.model.ProductModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(ProductModel model);

    ProductModel toModel(ProductDTO dto);

    List<ProductDTO> toDtoList(List<ProductModel> modelList);

    List<ProductModel> toModelList(List<ProductDTO> DTOList);
}
