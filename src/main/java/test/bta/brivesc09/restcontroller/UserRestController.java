package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {
    @Autowired
    private UserRestService userRestService;

    @Autowired
    private StaffRestService staffRestService;

    @Autowired
    private RoleDb roleDb;

    @Autowired
    private UserDb userDb;


    @PostMapping()
    @RequestMapping("/create")
    public BaseResponse<UserModel> createUserStaff(@Valid @RequestBody StaffDTO staff, BindingResult bindingResult) throws ParseException {
        BaseResponse<UserModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                    StaffModel newStaff = new StaffModel();
                    newStaff.setNoPegawai(staff.noPegawai);
                    newStaff.setTarif(staff.tarif);
                    newStaff = staffRestService.createStaff(newStaff);
                    UserModel newUser = new UserModel();

                    Long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                    newUser.setIdUser(uuid);
                    newUser.setUsername(staff.username);
                    newUser.setNamaLengkap(staff.namaLengkap);
                    newUser.setEmail(staff.email);
                    newUser.setPassword(staff.password); //belum enkripsi
                    newUser.setNoHP(staff.noHP);
                    newUser.setStaff(newStaff);

                    RoleModel role = roleDb.findByNamaRole(staff.getRole()).get();
                    newUser.setRole(role);
                    newUser.setStaff(newStaff);
                    userRestService.createUser(newUser); //disini error
                    newStaff.setUser(newUser);
                    newStaff = staffRestService.updateStaff(newStaff.getIdStaff(), newStaff);
                    response.setStatus(200);
                    response.setMessage("success");
                    response.setResult(newUser);
                    
            } catch (DataIntegrityViolationException e){
                response.setStatus(400);
                response.setMessage("Email telah terdaftar");
                response.setResult(null);
            } catch (Exception e) {
                response.setStatus(500);
                response.setMessage(e.toString());
                response.setResult(null);
            } 
            return response;
        }
    }

    @GetMapping()
    @RequestMapping("/all")
    public BaseResponse<List<UserModel>> getAllUser() {
        BaseResponse<List<UserModel>> response = new BaseResponse<>();

        response.setStatus(200);
        response.setMessage("success");
        response.setResult(userRestService.getAllUser());

        return response;
    }

}


