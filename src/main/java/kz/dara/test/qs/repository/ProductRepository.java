package kz.dara.test.qs.repository;

import kz.dara.test.qs.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    @Query(value = "select cc from ProductModel cc where cc.model like :productName or cc.description like :productName")
    List<ProductModel> searchProduct(@Param("productName")String productName);
}
