package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.StaffModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.UserModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffDb extends JpaRepository<StaffModel, Long>{
   Optional<StaffModel> findByIdStaff(Long idStaff);
   List<StaffModel> findByListJadwal_IdJadwal(Long idJadwal);
}
