package test.bta.brivesc09.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.repository.JadwalDb;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class JadwalRestServiceImpl implements JadwalRestService{
    @Autowired
    private JadwalDb jadwalDb;

    @Override
    public List<JadwalModel> getAllJadwal() {
        return jadwalDb.findAll();
    }

    @Override
    public JadwalModel createJadwal(JadwalModel jadwal) {
        return jadwalDb.save(jadwal);
    }

    @Override
    public List<JadwalModel> getListJadwalByTanggal(LocalDate tanggal) {
        return jadwalDb.findByTanggal(tanggal);
    }

    @Override
    public JadwalModel getJadwalById(Long idJadwal) {
        Optional<JadwalModel> jadwal = jadwalDb.findByIdJadwal(idJadwal);
        if (jadwal.isPresent()) {
            return jadwal.get();
        }
        return null;
    }
}
