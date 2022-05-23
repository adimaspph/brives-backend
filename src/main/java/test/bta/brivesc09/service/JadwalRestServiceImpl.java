package test.bta.brivesc09.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.JadwalDb;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
            if (jadwal.getJenisKelas().toString() == "TAMBAHAN") continue;
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

    @Override
    public PesananModel getVerifiedPesanan(Long idJadwal) {
        JadwalModel jadwal = getJadwalById(idJadwal);
        for (PesananModel pesanan :
                jadwal.getListPesanan()) {
            if (!pesanan.getStatus().getJenisStatus().equals("Belum Dibayar")  ) {
                if (!pesanan.getStatus().getJenisStatus().equals("Pembayaran Ditolak"))
                    if (!pesanan.getStatus().getJenisStatus().equals("Dibatalkan"))
                        return pesanan;
            }
        }
        return null;
    }

    @Override
    public List<HashMap<String,String>> getAllKelasTambahanPerYear(String year){
        List<HashMap<String,String>> allTrans = new ArrayList<>();
        HashMap<String,String> jan = new HashMap<>();
        jan.put("name", "Jan");
        jan.put("kelasTambahan", "0");
        allTrans.add(jan);
        HashMap<String,String> feb = new HashMap<>();
        feb.put("name", "Feb");
        feb.put("kelasTambahan", "0");
        allTrans.add(feb);
        HashMap<String,String> mar = new HashMap<>();
        mar.put("name", "Mar");
        mar.put("kelasTambahan", "0");
        allTrans.add(mar);
        HashMap<String,String> apr = new HashMap<>();
        apr.put("name", "Apr");
        apr.put("kelasTambahan", "0");
        allTrans.add(apr);
        HashMap<String,String> may = new HashMap<>();
        may.put("name", "May");
        may.put("kelasTambahan", "0");
        allTrans.add(may);
        HashMap<String,String> jun = new HashMap<>();
        jun.put("name", "Jun");
        jun.put("kelasTambahan", "0");
        allTrans.add(jun);
        HashMap<String,String> jul = new HashMap<>();
        jul.put("name", "Jul");
        jul.put("kelasTambahan", "0");
        allTrans.add(jul);
        HashMap<String,String> aug = new HashMap<>();
        aug.put("name", "Aug");
        aug.put("kelasTambahan", "0");
        allTrans.add(aug);
        HashMap<String,String> sep = new HashMap<>();
        sep.put("name", "Sep");
        sep.put("kelasTambahan", "0");
        allTrans.add(sep);
        HashMap<String,String> oct = new HashMap<>();
        oct.put("name", "Oct");
        oct.put("kelasTambahan", "0");
        allTrans.add(oct);
        HashMap<String,String> nov = new HashMap<>();
        nov.put("name", "Nov");
        nov.put("kelasTambahan", "0");
        allTrans.add(nov);
        HashMap<String,String> des = new HashMap<>();
        des.put("name", "Des");
        des.put("kelasTambahan", "0");
        allTrans.add(des);

        List<JadwalModel> allJadwal = jadwalDb.findAll();
        for (JadwalModel jadwal : allJadwal) {
            if (!jadwal.getJenisKelas().toString().equals("TAMBAHAN")) {
                continue;
            }
            LocalDateTime waktuPesan = jadwal.getTanggal().atStartOfDay();
            Date date = Timestamp.valueOf(waktuPesan);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String[] qu = cal.getTime().toString().split(" ");
            
            if (qu[qu.length-1].equals(year)) {
                for (HashMap<String,String> trans : allTrans) {
                    if (trans.get("name").equals(qu[1])) {
                        Long x = Long.parseLong(trans.get("kelasTambahan"));
                        x++;
                        String totalNominal = String.valueOf(x);
                        trans.put("kelasTambahan", totalNominal);
                    }
                }
            }
        }
        return allTrans;
    }
}
