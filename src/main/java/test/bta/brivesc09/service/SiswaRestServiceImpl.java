package test.bta.brivesc09.service;

import test.bta.brivesc09.model.SiswaModel;
import test.bta.brivesc09.repository.SiswaDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SiswaRestServiceImpl implements SiswaRestService {
   @Autowired
   private SiswaDb siswaDb;

   @Override
   public SiswaModel createSiswa(SiswaModel siswa) {
       return siswaDb.save(siswa);
   }

   @Override
   public SiswaModel updateSiswa(Long idSiswa, SiswaModel siswa) {
    SiswaModel oldSiswa = siswaDb.findByIdSiswa(idSiswa).get();
    oldSiswa.setAsalSekolah(siswa.getAsalSekolah());
    oldSiswa.setListJadwal(siswa.getListJadwal());
    oldSiswa.setListPesanan(siswa.getListPesanan());
    oldSiswa.setUser(siswa.getUser());
    
    return siswaDb.save(oldSiswa);
   }

   @Override
   public List<SiswaModel> getAllSiswa() {
       return siswaDb.findAll();
   }

   @Override
   public SiswaModel getSiswaByIdSiswa(Long idSiswa) {
       Optional<SiswaModel> siswa = siswaDb.findByIdSiswa(idSiswa);
       if (siswa.isPresent()) {
           return siswa.get();
       }
       return null;
   }

   @Override
   public void deleteSiswa(Long idSiswa) {
       SiswaModel Siswa = getSiswaByIdSiswa(idSiswa);
       siswaDb.delete(Siswa);
   }

   
}