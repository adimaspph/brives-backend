package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.MapelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.PesananModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface PesananDb extends JpaRepository<PesananModel, Long>{
    PesananModel findByIdPesanan(Long idPesanan);
//    Optional<PesananModel> findByNamaMapel(String namaMapel);
    List<PesananModel> findAll();
//    List<MapelModel> findAllByNamaMapel(String namaMapel);
//    void deleteByIdMapel(Long idMapel);
}
