package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.MapelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.StatusPesananModel;
import test.bta.brivesc09.model.UserModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PesananDb extends JpaRepository<PesananModel, Long>{
    PesananModel findByIdPesanan(Long idPesanan);
    List<PesananModel> findAll();
//    List<PesananModel> findByStatus_ListPesanan_IdStatus(Long idStatus);

    List<PesananModel> findByStatus_IdStatusPesanan(Long idStatusPesanan);
    List<PesananModel> findBySiswa_IdSiswa(Long idSiswa);
    List<PesananModel> findByJadwal_IdJadwal_AndStatus_IdStatusPesanan(Long idJadwal, Long idStatusPesanan);
    List<PesananModel> findByWaktuDibuatBetween(LocalDateTime start, LocalDateTime end);

}
