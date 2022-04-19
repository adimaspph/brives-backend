package test.bta.brivesc09.repository;

import org.springframework.security.core.userdetails.User;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDb extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);

    UserModel findByIdUser(Long idUser);

    List<UserModel> findByStaff_ListMapel_IdMapel(Long idMapel);
    List<UserModel> findBySiswa_IdSiswa(Long idSiswa);
    List<UserModel> findByStaff_IdStaff(Long idStaff);

}
