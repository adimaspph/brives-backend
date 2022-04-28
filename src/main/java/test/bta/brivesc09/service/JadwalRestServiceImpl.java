package test.bta.brivesc09.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.JadwalDb;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class JadwalRestServiceImpl implements JadwalRestService{
    @Autowired
    private JadwalDb jadwalDb;

    @Autowired
    private MapelRestService mapelRestService;

    @Override
    public List<JadwalModel> getAllJadwal() {
        return jadwalDb.findAll();
    }

    @Override
    public JadwalModel createJadwal(JadwalModel jadwal) {
        jadwal.setWaktuSelesai(jadwal.getWaktuMulai().plusMinutes(90));

        LocalDate dateNow = LocalDate.now();
        if (jadwal.getTanggal().isBefore(dateNow)) {
            throw new UnsupportedOperationException("Tidak dapat membuat jadwal kemarin");
        }

        List<JadwalModel> listJadwalLama = jadwalDb.findByTanggalAndStaff(jadwal.getTanggal(), jadwal.getStaff());

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
    public List<JadwalModel> getListJadwalByTanggal(LocalDate tanggal, StaffModel staff) {
        List<JadwalModel> result = new ArrayList<>();
        List<JadwalModel> listJadwal = jadwalDb.findByTanggalAndStaff(tanggal, staff);
        return listJadwal;
    }

    @Override
    public JadwalModel getJadwalById(Long idJadwal) {
        return jadwalDb.findByIdJadwal(idJadwal);
    }

    @Override
    public Boolean deleteJadwalById(Long idJadwal) {
        JadwalModel jadwal = jadwalDb.findByIdJadwal(idJadwal);

        if (jadwal.getSiswa() != null) {
            throw new UnsupportedOperationException("Jadwal telah dibooking");
        }
        jadwalDb.deleteByIdJadwal(idJadwal);
        return true;
    }

    @Override
    public List<JadwalModel> getAllJadwalByIdMapel(Long idMapel, LocalDate tanggal) {
        List<JadwalModel> result = new ArrayList<>();
        for (JadwalModel jadwal: jadwalDb.findByTanggalAndMapel(tanggal, mapelRestService.getMapelById(idMapel))) {
            if (jadwal.getListPesanan().isEmpty()) {
                result.add(jadwal);
            } else {
                boolean booked = false;
                for (PesananModel pesanan : jadwal.getListPesanan()) {
                    Long status = pesanan.getStatus().getIdStatusPesanan();
                    if (status != 7) {
                        if (status != 4) {
                            booked = true;
                        }
                    }
                }
                if (!booked) {
                    result.add(jadwal);
                }
            }
        }
        return result;
    }
}
