package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.JenjangModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.repository.MapelDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MapelRestServiceImpl implements MapelRestService {
    @Autowired
    private MapelDb mapelDb;

    @Override
    public MapelModel createMapel(MapelModel mapel) {
        return mapelDb.save(mapel);
    }

    @Override
    public MapelModel getMapelById(Long id) {
        return mapelDb.findByIdMapel(id);
    }

    @Override
    public List<MapelModel> getAllMapelByIdJenang(Long idJenjang) {
        List<MapelModel> allMapel = mapelDb.findAll();
        List<MapelModel> result = new ArrayList<>();

        for (MapelModel mapel : allMapel) {
            for (JenjangModel jenjang : mapel.getListJenjang()) {
                if (jenjang.getIdJenjang().equals(idJenjang)) {
                    result.add(mapel);
                }
            }
        }
        return result;
    }

    @Override
    public MapelModel deleteByIdMapel(Long idMapel) {
        MapelModel mapel = getMapelById(idMapel);
        mapelDb.deleteByIdMapel(idMapel);
        return mapel;
    }

}
