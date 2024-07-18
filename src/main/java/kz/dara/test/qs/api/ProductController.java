package kz.dara.test.qs.api;

import kz.dara.test.qs.dto.ProductDTO;
import kz.dara.test.qs.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    @GetMapping()
    public List<ProductDTO> getProducts(){
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable(name = "id")Long id){
        return service.getProduct(id);
    }

    @PostMapping()
    public ProductDTO addProduct(@RequestBody ProductDTO model){
        return service.addProduct(model);
    }

    @PutMapping
    public ProductDTO editProduct(@RequestBody ProductDTO model){
        return service.editProduct(model);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(name = "id")Long id){
        service.deleteProduct(id);
    }
}
