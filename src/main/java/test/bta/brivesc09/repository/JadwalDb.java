package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.JadwalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JadwalDb extends JpaRepository<JadwalModel, Long>{
    Optional<JadwalModel> findByIdJadwal(Long idJenjang);
    List<JadwalModel> findByTanggal(LocalDate tanggal);
    List<JadwalModel> findAll();

}
