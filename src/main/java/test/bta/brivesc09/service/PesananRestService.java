package test.bta.brivesc09.service;

import test.bta.brivesc09.model.PesananModel;
import java.util.List;

public interface PesananRestService {
    PesananModel createPesanan(PesananModel pesanan);
    PesananModel getPesananById(Long id);
//    PesananModel deleteByIdMapel(Long idMapel);
//    PesananModel getMapelById(Long id);
}
