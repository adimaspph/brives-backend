package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.model.UserModel;

import java.time.LocalDate;
import java.util.List;

public interface JadwalRestService {
    List<JadwalModel> getAllJadwal();
    JadwalModel createJadwal(JadwalModel jadwal);
    List<JadwalModel> getListJadwalByTanggal(LocalDate tanggal, StaffModel staff);
    JadwalModel getJadwalById(Long idJadwal);
}
