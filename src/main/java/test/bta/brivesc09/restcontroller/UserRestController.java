package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.StaffDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.SiswaDTO;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.SiswaRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import java.util.*;

import javax.persistence.EmbeddedId;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@CrossOrigin(origins = "https://brives-staging.herokuapp.com")
@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {
    @Autowired
    private UserRestService userRestService;

    @Autowired
    private StaffRestService staffRestService;

    @Autowired
    private SiswaRestService siswaRestService;

    @Autowired
    private MapelRestService mapelRestService;

    @Autowired
    private RoleDb roleDb;

    @Autowired
    private StaffDb staffDb;

    @Autowired
    private UserDb userDb;

    @Autowired
    private MapelDb mapelDb;


    @PostMapping("/create")
    public BaseResponse<UserModel> createUserStaff(@Valid @RequestBody StaffDTO staff, BindingResult bindingResult) throws ParseException {
        BaseResponse<UserModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                String allErrorMessage = userRestService.checkConditions(staff);
                
                if (!allErrorMessage.equals("")) {
                    response.setStatus(999);
                    response.setMessage(allErrorMessage);
                    response.setResult(null);
                    return response;
                }
                StaffModel newStaff = new StaffModel();
                newStaff.setNoPegawai(staff.getNoPegawai());
                newStaff.setTarif(0);
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
                List<MapelModel> mapels = new ArrayList<>();
                Integer tarif = 0;
                newStaff.setTarif(staff.getTarif());
                if (staff.getRole().equalsIgnoreCase("PENGAJAR")) {
                    for (String mapel : staff.getListMapel()) {
                        MapelModel mataPelajaran = mapelDb.findByNamaMapel(mapel).get();
                        List<StaffModel> temp = mataPelajaran.getListStaff();
                        temp.add(newStaff);
                        mataPelajaran.setListStaff(temp);
                        mapelDb.save(mataPelajaran);
                        mapels.add(mataPelajaran);
                    }
                    tarif = staff.getTarif();
                    newStaff.setListMapel(mapels);
                    
                }
                newStaff.setTarif(tarif);
                
                userRestService.createUser(newUser);
                newStaff.setUser(newUser);
                newStaff = staffRestService.updateStaff(newStaff.getIdStaff(), newStaff);
                response.setStatus(200);
                response.setMessage("User berhasil terbuat");
                response.setResult(newUser);
                    
            } catch (DataIntegrityViolationException e){
                response.setStatus(400);
                response.setMessage("Username telah terdaftar");
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



    @GetMapping("/auth")
    public BaseResponse<UserModel> getAuthenticatedUser(HttpServletRequest request) throws Exception {
        BaseResponse<UserModel> response = new BaseResponse<>();

        try {
            UserModel authUser = userRestService.getUserFromJwt(request);
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(authUser);

            return response;
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("internal server error");
            response.setResult(null);

            return response;
        }

        
    }

    @DeleteMapping("/{username}")
    public BaseResponse<UserModel> deleteUser(@PathVariable String username) {
        BaseResponse<UserModel> response = new BaseResponse<>();

        try{
            response.setStatus(200);
            response.setMessage("success");
            UserModel user = userRestService.getUserByUsername(username);
            userRestService.deleteUser(user);

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }



    @GetMapping("/{username}")
    public BaseResponse<UserModel> getUserById(@PathVariable String username) {
        BaseResponse<UserModel> response = new BaseResponse<>();
        try{
            UserModel user = userRestService.getUserByUsername(username);
            if (user != null){
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(user);
            } else {
                response.setStatus(400);
                response.setMessage("null");
                response.setResult(null);
            }

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @GetMapping("/role/{username}")
    public BaseResponse<RoleModel> getUserRole(@PathVariable String username) {
        BaseResponse<RoleModel> response = new BaseResponse<>();
        try{
            UserModel user = userRestService.getUserByUsername(username);

            response.setStatus(200);
            response.setMessage("success");
            response.setResult(user.getRole());

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @PostMapping("/create/akun")
    public BaseResponse<UserModel> createAkunPelajar(@Valid @RequestBody SiswaDTO siswa, BindingResult bindingResult) throws Exception {
        BaseResponse<UserModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                String allErrorMessage = userRestService.checkConditions(siswa);
                
                if (!allErrorMessage.equals("")) {
                    response.setStatus(999);
                    response.setMessage(allErrorMessage);
                    response.setResult(null);
                    return response;
                }
                SiswaModel newSiswa = new SiswaModel();
                newSiswa.setAsalSekolah(siswa.getAsalSekolah());
                newSiswa = siswaRestService.createSiswa(newSiswa);
                UserModel newUser = new UserModel();

                Long uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                newUser.setIdUser(uuid);
                newUser.setUsername(siswa.getUsername());
                newUser.setNamaLengkap(siswa.getNamaLengkap());
                newUser.setEmail(siswa.getEmail());
                newUser.setPassword(userRestService.encrypt(siswa.getPassword()));
                newUser.setNoHP(siswa.getNoHP());
                newUser.setSiswa(newSiswa);

                RoleModel role = roleDb.findByNamaRole("PELAJAR").get();
                newUser.setRole(role);
                newUser.setSiswa(newSiswa);
                
                userRestService.createUser(newUser);
                newSiswa.setUser(newUser);
                newSiswa = siswaRestService.updateSiswa(newSiswa.getIdSiswa(), newSiswa);
                response.setStatus(200);
                response.setMessage("User berhasil terbuat");
                response.setResult(newUser);
                    
            } catch (DataIntegrityViolationException e){
                response.setStatus(400);
                response.setMessage("Username telah terdaftar");
                response.setResult(null);
            } catch (Exception e) {
                response.setStatus(500);
                response.setMessage(e.toString());
                response.setResult(null);
            } 
            return response;
        }
        
    }

    @GetMapping("/siswa/{id}")
    public BaseResponse<List<UserModel>> getUserBySiswa(@PathVariable Long id) {
        BaseResponse<List<UserModel>> response = new BaseResponse<>();
        try {
            List<UserModel> data = userDb.findBySiswa_IdSiswa(id);
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(data);
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @GetMapping("/staff/{id}")
    public BaseResponse<List<UserModel>> getUserByStaff(@PathVariable Long id) {
        BaseResponse<List<UserModel>> response = new BaseResponse<>();
        try {
            List<UserModel> data = userDb.findByStaff_IdStaff(id);
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(data);
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }
        
    



}


