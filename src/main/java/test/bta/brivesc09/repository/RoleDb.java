package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDb extends JpaRepository<RoleModel, Long>{
   Optional<RoleModel> findByNamaRole(String name);
   RoleModel findAllByIdRole(Long idRole);
}
