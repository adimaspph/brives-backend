package test.bta.brivesc09.service;

import test.bta.brivesc09.model.MapelModel;
import java.util.List;

public interface MapelRestService {
    MapelModel createMapel(MapelModel mapel);
    //    List<MapelModel> getListMapel();
//    MapelModel getStaffByIdStaff(Long idStaff);
//    MapelModel updateStaff(Long idStaff, MapelModel StaffUpdate);
//    void deleteStaff(Long idStaff);
    MapelModel getMapelById(Long id);
}
