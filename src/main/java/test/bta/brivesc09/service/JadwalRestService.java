package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;

import java.time.LocalDate;
import java.util.List;

public interface JadwalRestService {
    List<JadwalModel> getAllJadwal();
    JadwalModel createJadwal(JadwalModel jadwal);
    List<JadwalModel> getListJadwalByTanggal(LocalDate tanggal);
    JadwalModel getJadwalById(Long idJadwal);
}
