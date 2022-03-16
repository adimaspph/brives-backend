package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.MapelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapelDb extends JpaRepository<MapelModel, Long>{
    Optional<MapelModel> findByIdMapel(Long idMapel);
    List<MapelModel> findAll();
//    List<MapelModel> findAllByJenjang(String jenjang);

}
