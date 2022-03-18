//package test.bta.brivesc09.service;
//
//import test.bta.brivesc09.model.StaffModel;
//import test.bta.brivesc09.repository.StaffDb;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class StaffRestServiceImpl implements StaffRestService {
//    @Autowired
//    private StaffDb staffDb;
//
//    @Override
//    public StaffModel createStaff(StaffModel staff) {
//        return staffDb.save(staff);
//    }
//
//    @Override
//    public List<StaffModel> getAllStaff() {
//        return staffDb.findAll();
//    }
//
//    @Override
//    public StaffModel getStaffByIdStaff(Long idStaff) {
//        Optional<StaffModel> staff = staffDb.findByIdStaff(idStaff);
//        if (staff.isPresent()) {
//            return staff.get();
//        }
//        return null;
//    }
//
//    @Override
//    public StaffModel updateStaff(Long idStaff, StaffModel staffUpdate) {
//        StaffModel staff = getStaffByIdStaff(idStaff);
//        staff.setPassword(staffUpdate.getPassword());
//        staff.setNamaLengkap(staffUpdate.getNamaLengkap());
//        staff.setNoPegawai(staffUpdate.getNoPegawai());
//        staff.setNoHp(staffUpdate.getNoHp());
//        return staffDb.save(staff);
//    }
//
//    @Override
//    public void deleteStaff(Long idStaff) {
//        StaffModel staff = getStaffByIdStaff(idStaff);
//        staffDb.delete(staff);
//    }
//}