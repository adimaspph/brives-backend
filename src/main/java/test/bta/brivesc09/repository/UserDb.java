package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserDb extends JpaRepository<UserModel, Long>{
   Optional<UserModel> findByUsername(String username);
   UserModel findByIdUser(Long idUser);
}
