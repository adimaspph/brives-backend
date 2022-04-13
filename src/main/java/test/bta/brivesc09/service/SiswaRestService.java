package test.bta.brivesc09.service;

import test.bta.brivesc09.model.SiswaModel;
import java.util.List;

public interface SiswaRestService {
   SiswaModel createSiswa(SiswaModel siswa);
   SiswaModel updateSiswa(Long idSiswa, SiswaModel siswa);
   List<SiswaModel> getAllSiswa();
   SiswaModel getSiswaByIdSiswa(Long idSiswa);
   void deleteSiswa(Long idSiswa);
}
