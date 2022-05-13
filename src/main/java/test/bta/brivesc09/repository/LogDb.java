package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.LogModel;
import test.bta.brivesc09.model.MapelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.UserModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogDb extends JpaRepository<LogModel, Long>{
    LogModel findByIdLog(Long idLog);
    List<LogModel> findAll();
    List<LogModel> findByStaff_IdStaff(Long idSiswa);
    List<LogModel> findByStatusKehadiran(String statusKehadiran);
}
