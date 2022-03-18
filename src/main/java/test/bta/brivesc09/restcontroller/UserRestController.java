package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.ArrayList;
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
    private MapelDb mapelDb;


    @PostMapping("/create")
    public BaseResponse<UserModel> createUserStaff(@Valid @RequestBody StaffDTO staff, BindingResult bindingResult) throws ParseException {
        BaseResponse<UserModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                    StaffModel newStaff = new StaffModel();
                    newStaff.setNoPegawai(staff.getNoPegawai());
                    newStaff.setTarif(staff.getTarif());
                    newStaff = staffRestService.createStaff(newStaff);
                    UserModel newUser = new UserModel();

                    Long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                    newUser.setIdUser(uuid);
                    newUser.setUsername(staff.getUsername());
                    newUser.setNamaLengkap(staff.getNamaLengkap());
                    newUser.setEmail(staff.getEmail());
                    newUser.setPassword(userRestService.encrypt(staff.getPassword()));
                    newUser.setNoHP(staff.getNoHP());
                    newUser.setStaff(newStaff);

                    RoleModel role = roleDb.findByNamaRole(staff.getRole()).get();
                    newUser.setRole(role);
                    newUser.setStaff(newStaff);

                    if (staff.getRole().equalsIgnoreCase("pengajar")) {
                        List<MapelModel> mapels = new ArrayList<>();
                        for (String mapel : staff.listMapel) {
                            MapelModel mataPelajaran = mapelDb.findByNamaMapel(mapel).get();
                            mapels.add(mataPelajaran);
                        }
                        newStaff.setListMapel(mapels);
                    }
                    
                    userRestService.createUser(newUser);
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

    @GetMapping("/all")
    public BaseResponse<List<UserModel>> getAllUser() {
        BaseResponse<List<UserModel>> response = new BaseResponse<>();

        response.setStatus(200);
        response.setMessage("success");
        response.setResult(userRestService.getAllUser());

        return response;
    }

}


