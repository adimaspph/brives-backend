//package test.bta.brivesc09.restcontroller;
//
//import test.bta.brivesc09.rest.BaseResponse;
//import test.bta.brivesc09.rest.StaffDTO;
//import test.bta.brivesc09.model.StaffModel;
//import test.bta.brivesc09.repository.StaffDb;
//import test.bta.brivesc09.service.StaffRestService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.publisher.Mono;
//import java.text.ParseException;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@RestController
//@RequestMapping("/api/v1/staf")
//public class StaffRestController {
//    @Autowired
//    private StaffRestService staffRestService;
//
//    @PostMapping()
//    @RequestMapping("/create")
//    public BaseResponse<StaffModel> createStaff(@Valid @RequestBody StaffDTO staff, BindingResult bindingResult) throws ParseException {
//        BaseResponse<StaffModel> response = new BaseResponse<>();
//        if (bindingResult.hasFieldErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
//        } else {
//            try {
//                StaffModel newStaff = new StaffModel();
//                newStaff.setIdStaff(staff.idStaff);
//                newStaff.setEmail(staff.email);
//                newStaff.setPassword(staff.password);
//                newStaff.setNamaLengkap(staff.namaLengkap);
//                newStaff.setNoPegawai(staff.noPegawai);
//                newStaff.setNoHp(staff.noHp);
//                newStaff.setRole(staff.role);
//                StaffModel savedStaff = staffRestService.createStaff(newStaff);
//                response.setStatus(200);
//                response.setMessage("success");
//                response.setResult(savedStaff);
//            } catch (Exception e) {
//                response.setStatus(400);
//                response.setMessage(e.toString());
//                response.setResult(null);
//            }
//            return response;
//        }
//    }
//
//    @GetMapping()
//    @RequestMapping("/all")
//    public BaseResponse<List<StaffModel>> getAllStaff() {
//        BaseResponse<List<StaffModel>> response = new BaseResponse<>();
//
//
//        response.setStatus(200);
//        response.setMessage("success");
//        response.setResult(staffRestService.getAllStaff());
//
//        return response;
//    }
//
//}
//
//
//
