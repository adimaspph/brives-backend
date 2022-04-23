package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.repository.MapelDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.repository.PesananDb;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PesananRestServiceImpl implements PesananRestService {
    @Autowired
    private PesananDb pesananDb;

    @Override
    public PesananModel createPesanan(PesananModel pesanan) {
        return pesananDb.save(pesanan);
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
