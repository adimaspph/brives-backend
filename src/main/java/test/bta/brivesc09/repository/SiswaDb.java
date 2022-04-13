package test.bta.brivesc09.repository;

import test.bta.brivesc09.model.SiswaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SiswaDb extends JpaRepository<SiswaModel, Long>{
   Optional<SiswaModel> findByIdSiswa(Long idSiswa);
}
