package test.bta.brivesc09.service;

import test.bta.brivesc09.model.PesananModel;

import java.util.HashMap;
import java.util.List;

public interface PesananRestService {
    PesananModel createPesanan(PesananModel pesanan);
    PesananModel getPesananById(Long id);
    List<HashMap<String,String>> getAllTransactionPerYear(String year);
    List<HashMap<String,String>> getAllKelasPrivatPerYear(String year);
    List<HashMap<String,String>> getAllKelasTambahanPerYear(String year);
//    PesananModel deleteByIdMapel(Long idMapel);
//    PesananModel getMapelById(Long id);
}
