package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.MapelDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.repository.PesananDb;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PesananRestServiceImpl implements PesananRestService {
    @Autowired
    private PesananDb pesananDb;

    @Autowired
    private JadwalDb jadwalDb;

    @Override
    public PesananModel createPesanan(PesananModel pesanan) {
        return pesananDb.save(pesanan);
    }

    @Override
    public PesananModel getPesananById(Long id) {
        return pesananDb.getById(id);
    }

    @Override
    public List<HashMap<String,String>> getAllTransactionPerYear(String year) {
        List<HashMap<String,String>> allTrans = new ArrayList<>();
        HashMap<String,String> jan = new HashMap<>();
        jan.put("name", "Jan");
        jan.put("pendapatan", "0");
        allTrans.add(jan);
        HashMap<String,String> feb = new HashMap<>();
        feb.put("name", "Feb");
        feb.put("pendapatan", "0");
        allTrans.add(feb);
        HashMap<String,String> mar = new HashMap<>();
        mar.put("name", "Mar");
        mar.put("pendapatan", "0");
        allTrans.add(mar);
        HashMap<String,String> apr = new HashMap<>();
        apr.put("name", "Apr");
        apr.put("pendapatan", "0");
        allTrans.add(apr);
        HashMap<String,String> may = new HashMap<>();
        may.put("name", "May");
        may.put("pendapatan", "0");
        allTrans.add(may);
        HashMap<String,String> jun = new HashMap<>();
        jun.put("name", "Jun");
        jun.put("pendapatan", "0");
        allTrans.add(jun);
        HashMap<String,String> jul = new HashMap<>();
        jul.put("name", "Jul");
        jul.put("pendapatan", "0");
        allTrans.add(jul);
        HashMap<String,String> aug = new HashMap<>();
        aug.put("name", "Aug");
        aug.put("pendapatan", "0");
        allTrans.add(aug);
        HashMap<String,String> sep = new HashMap<>();
        sep.put("name", "Sep");
        sep.put("pendapatan", "0");
        allTrans.add(sep);
        HashMap<String,String> oct = new HashMap<>();
        oct.put("name", "Oct");
        oct.put("pendapatan", "0");
        allTrans.add(oct);
        HashMap<String,String> nov = new HashMap<>();
        nov.put("name", "Nov");
        nov.put("pendapatan", "0");
        allTrans.add(nov);
        HashMap<String,String> des = new HashMap<>();
        des.put("name", "Des");
        des.put("pendapatan", "0");
        allTrans.add(des);

        List<PesananModel> allPesanan = pesananDb.findAll();
        for (PesananModel pesanan : allPesanan) {
            LocalDateTime waktuPesan = pesanan.getWaktuDibuat();
            Date date = Timestamp.valueOf(waktuPesan);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String[] qu = cal.getTime().toString().split(" ");
            if (qu[qu.length-1].equals(year)) {
                for (HashMap<String,String> trans : allTrans) {
                    if (trans.get("name").equals(qu[1])) {
                        Long x = Long.parseLong(trans.get("pendapatan"));
                        x += pesanan.getNominal();
                        String totalNominal = String.valueOf(x);
                        trans.put("pendapatan", totalNominal);
                    }
                }
            }
        }
        return allTrans;
    }

    @Override
    public List<HashMap<String,String>> getAllKelasPrivatPerYear(String year){
        List<HashMap<String,String>> allTrans = new ArrayList<>();
        HashMap<String,String> jan = new HashMap<>();
        jan.put("name", "Jan");
        jan.put("kelasPrivat", "0");
        allTrans.add(jan);
        HashMap<String,String> feb = new HashMap<>();
        feb.put("name", "Feb");
        feb.put("kelasPrivat", "0");
        allTrans.add(feb);
        HashMap<String,String> mar = new HashMap<>();
        mar.put("name", "Mar");
        mar.put("kelasPrivat", "0");
        allTrans.add(mar);
        HashMap<String,String> apr = new HashMap<>();
        apr.put("name", "Apr");
        apr.put("kelasPrivat", "0");
        allTrans.add(apr);
        HashMap<String,String> may = new HashMap<>();
        may.put("name", "May");
        may.put("kelasPrivat", "0");
        allTrans.add(may);
        HashMap<String,String> jun = new HashMap<>();
        jun.put("name", "Jun");
        jun.put("kelasPrivat", "0");
        allTrans.add(jun);
        HashMap<String,String> jul = new HashMap<>();
        jul.put("name", "Jul");
        jul.put("kelasPrivat", "0");
        allTrans.add(jul);
        HashMap<String,String> aug = new HashMap<>();
        aug.put("name", "Aug");
        aug.put("kelasPrivat", "0");
        allTrans.add(aug);
        HashMap<String,String> sep = new HashMap<>();
        sep.put("name", "Sep");
        sep.put("kelasPrivat", "0");
        allTrans.add(sep);
        HashMap<String,String> oct = new HashMap<>();
        oct.put("name", "Oct");
        oct.put("kelasPrivat", "0");
        allTrans.add(oct);
        HashMap<String,String> nov = new HashMap<>();
        nov.put("name", "Nov");
        nov.put("kelasPrivat", "0");
        allTrans.add(nov);
        HashMap<String,String> des = new HashMap<>();
        des.put("name", "Des");
        des.put("kelasPrivat", "0");
        allTrans.add(des);

        List<PesananModel> allPesanan = pesananDb.findAll();
        for (PesananModel pesanan : allPesanan) {
            if (!pesanan.getJadwal().getJenisKelas().toString().equals("PRIVATE")) {
                continue;
            }
            LocalDateTime waktuPesan = pesanan.getWaktuDibuat();
            Date date = Timestamp.valueOf(waktuPesan);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String[] qu = cal.getTime().toString().split(" ");
            if (qu[qu.length-1].equals(year)) {
                for (HashMap<String,String> trans : allTrans) {
                    if (trans.get("name").equals(qu[1])) {
                        Long x = Long.parseLong(trans.get("kelasPrivat"));
                        x++;
                        String totalNominal = String.valueOf(x);
                        trans.put("kelasPrivat", totalNominal);
                    }
                }
            }
        }
        return allTrans;
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

        List<PesananModel> allPesanan = pesananDb.findAll();
        for (PesananModel pesanan : allPesanan) {
            if (!pesanan.getJadwal().getJenisKelas().toString().equals("TAMBAHAN")) {
                continue;
            }
            LocalDateTime waktuPesan = pesanan.getWaktuDibuat();
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

//    @Override
//    public MapelModel createMapel(MapelModel mapel) {
//        return mapelDb.save(mapel);
//    }
//
//    @Override
//    public MapelModel getMapelById(Long id) {
//        return mapelDb.findByIdMapel(id);
//    }
//
//    @Override
//    public MapelModel deleteByIdMapel(Long idMapel) {
//        MapelModel mapel = getMapelById(idMapel);
//        mapelDb.deleteByIdMapel(idMapel);
//        return mapel;
//    }

}
