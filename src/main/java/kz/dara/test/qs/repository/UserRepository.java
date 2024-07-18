package kz.dara.test.qs.repository;


import kz.dara.test.qs.model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);

}
