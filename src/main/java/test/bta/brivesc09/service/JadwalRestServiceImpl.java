package test.bta.brivesc09.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        jadwal.setWaktuSelesai(jadwal.getWaktuMulai().plusMinutes(90));

        List<JadwalModel> listJadwalLama = jadwalDb.findByTanggal(jadwal.getTanggal());

        for (JadwalModel jadwalLama : listJadwalLama) {
            if (jadwal.getWaktuMulai().equals(jadwalLama.getWaktuMulai())) {
                throw new UnsupportedOperationException("Jadwal bertabrakan dengan jadwal yang telah ada");
            }
            if (jadwal.getWaktuSelesai().equals(jadwalLama.getWaktuSelesai())) {
                throw new UnsupportedOperationException("Jadwal bertabrakan dengan jadwal yang telah ada");
            }
            if (jadwal.getWaktuMulai().isBefore(jadwalLama.getWaktuSelesai()) && jadwal.getWaktuMulai().isAfter(jadwalLama.getWaktuMulai()) ){
                throw new UnsupportedOperationException("Jadwal bertabrakan dengan jadwal yang telah ada");
            }
            if (jadwal.getWaktuSelesai().isBefore(jadwalLama.getWaktuSelesai()) && jadwal.getWaktuSelesai().isAfter(jadwalLama.getWaktuMulai()) ){
                throw new UnsupportedOperationException("Jadwal bertabrakan dengan jadwal yang telah ada");
            }
        }
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
