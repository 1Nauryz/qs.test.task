package kz.dara.test.qs.service;

import kz.dara.test.qs.dto.ProductDTO;
import kz.dara.test.qs.mapper.ProductMapper;
import kz.dara.test.qs.model.ProductModel;
import kz.dara.test.qs.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    private final ProductMapper mapper;

    private final FileStorageService fileStorageService;

    public List<ProductDTO> getProducts() {
        return mapper.toDtoList(repository.findAll());
    }

    public ProductDTO getProduct(Long id){
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    public List<ProductModel> searchProduct(String key){
        return repository.searchProduct("%" + key + "%");
    }

    public ProductModel findProduct(Long id){
        return repository.findById(id).orElse(null);
    }

    public ProductModel addProduct(String model, String color, String description, Long price, MultipartFile image, String imagesPath) throws IOException {
        ProductModel productModel = new ProductModel();
        productModel.setModel(model);
        productModel.setColor(color);
        productModel.setDescription(description);
        productModel.setPrice(price);

        if (!image.isEmpty()) {
            String fileName = fileStorageService.saveFile(image, imagesPath);
            productModel.setImage(fileName);
        }

        return repository.save(productModel);
    }

    public ProductDTO addProduct(ProductDTO product){
        return mapper.toDto(repository.save(mapper.toModel(product)));
    }

    public ProductDTO editProduct(ProductDTO product){
        return mapper.toDto(repository.save(mapper.toModel(product)));
    }

    public ProductModel editProduct(ProductModel product){
        return repository.save(product);
    }

    public ProductModel saveProduct(Long id, String model, String color, String description, Long price, MultipartFile image, String imagesPath) throws IOException {
        ProductModel productModel = repository.findById(id).orElse(null);

        if (productModel != null) {
            productModel.setModel(model);
            productModel.setColor(color);
            productModel.setDescription(description);
            productModel.setPrice(price);

            if (!image.isEmpty()) {
                String fileName = fileStorageService.saveFile(image, imagesPath);
                productModel.setImage(fileName);
            }

            return repository.save(productModel);
        }

        return null;
    }

    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
