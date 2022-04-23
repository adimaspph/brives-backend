package test.bta.brivesc09.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.bta.brivesc09.model.StatusPesananModel;

@Repository
public interface StatusPesananDb extends JpaRepository<StatusPesananModel, Long> {
    StatusPesananModel findByIdStatusPesanan(Long idStatusPesanan);
}
