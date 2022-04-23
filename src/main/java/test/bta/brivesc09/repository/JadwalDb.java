package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.JadwalModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.StaffModel;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface JadwalDb extends JpaRepository<JadwalModel, Long>{
    JadwalModel findByIdJadwal(Long idJadwal);
    List<JadwalModel> findByTanggalAndStaff(LocalDate tanggal, StaffModel staff);
    List<JadwalModel> findAll();
    void deleteByIdJadwal(Long idMapel);
    List<JadwalModel> findByTanggalAndMapel(LocalDate tanggal, MapelModel mapel);
}
