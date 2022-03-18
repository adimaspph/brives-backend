package test.bta.brivesc09.service;

import test.bta.brivesc09.model.StaffModel;
import java.util.List;

public interface StaffRestService {
   StaffModel createStaff(StaffModel Staff);
   StaffModel updateStaff(Long idStaff, StaffModel Staff);
   List<StaffModel> getAllStaff();
   StaffModel getStaffByIdStaff(Long idStaff);
   void deleteStaff(Long idStaff);
}
