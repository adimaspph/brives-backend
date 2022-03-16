package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.JenjangModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JenjangDb extends JpaRepository<JenjangModel, Long>{
    Optional<JenjangModel> findByIdJenjang(Long idJenjang);
    List<JenjangModel> findAll();

}
