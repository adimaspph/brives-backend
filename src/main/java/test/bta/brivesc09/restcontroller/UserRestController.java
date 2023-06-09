package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.StaffDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.SiswaDTO;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.repository.JenjangDb;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.SiswaDb;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import test.bta.brivesc09.utils.S3Util;

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
    private SiswaDb siswaDb;

    @Autowired
    private MapelDb mapelDb;

    @Autowired
    private JenjangDb jenjangDb;


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
                JenjangModel jenjang = jenjangDb.findByNamaJenjang(siswa.getJenjang());
                newSiswa.setJenjang(jenjang);
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
    @PostMapping("/update/{username}")
    public BaseResponse<UserModel> updateProfilPelajar(@Valid @RequestBody SiswaDTO siswa, BindingResult bindingResult, @PathVariable String username) throws Exception {
        BaseResponse<UserModel> response = new BaseResponse<>();
        try {
            UserModel user = userRestService.getUserByUsername(username);
            SiswaModel pelajar = user.getSiswa();
            user.setNamaLengkap(siswa.getNamaLengkap());
            user.setNoHP(siswa.getNoHP());
            userDb.save(user);
            pelajar.setAsalSekolah(siswa.getAsalSekolah());

            if (!(siswa.getJenjang().equals(pelajar.getJenjang().getNamaJenjang()))) {
                JenjangModel jenjangSiswa = pelajar.getJenjang();
                List<SiswaModel> allSiswaInJenjang = jenjangSiswa.getListSiswa();
                allSiswaInJenjang.remove(pelajar);
                jenjangDb.save(jenjangSiswa);
                
                JenjangModel jenjang = jenjangDb.findByNamaJenjang(siswa.getJenjang());
                pelajar.setJenjang(jenjang);
            }
            siswaDb.save(pelajar);

            response.setStatus(200);
            response.setMessage("Akun berhasil terubah");
            response.setResult(user);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Akun tidak ditemukan");
            response.setResult(null);
        }

        return response;
    }

    @PostMapping("/update/staf/{username}")
    public BaseResponse<UserModel> updateProfilStaff(@Valid @RequestBody StaffDTO staff, BindingResult bindingResult, @PathVariable String username) throws Exception {
        BaseResponse<UserModel> response = new BaseResponse<>();
        try {
            UserModel user = userRestService.getUserByUsername(username);
            StaffModel staf = user.getStaff();
            user.setNamaLengkap(staff.getNamaLengkap());
            user.setNoHP(staff.getNoHP());

            RoleModel role = roleDb.findByNamaRole(staff.getRole()).get();
            user.setRole(role);
            userDb.save(user);
            staf.setNoPegawai(staff.getNoPegawai());
            staffDb.save(staf);
            List<MapelModel> mapels = staf.getListMapel();
            List<String> indexNama = new ArrayList<>();

            for (int i = 0; i < mapels.size(); i++) {
                MapelModel mapel = mapels.get(i);
                String namaMapelString = mapel.getNamaMapel();
                if (!staff.getListMapel().contains(namaMapelString)) {
                    indexNama.add(namaMapelString);
                }
            }
            for (String namaMapel : indexNama) {
                MapelModel mapel = mapelDb.findByNamaMapel(namaMapel).get();
                mapels.remove(mapel);
                staf.setListMapel(mapels);
                staffDb.save(staf);
                List<StaffModel> paraPengajar = mapel.getListStaff();
                paraPengajar.remove(staf);
                mapel.setListStaff(paraPengajar);
                mapelDb.save(mapel);
            }

            if (staff.getRole().equals("PENGAJAR")) {
                staf.setTarif(staff.getTarif());
                for (String mapel : staff.getListMapel()) {
                    MapelModel mataPelajaran = mapelDb.findByNamaMapel(mapel).get();
                    List<StaffModel> tempo = mataPelajaran.getListStaff();
                    if (!tempo.contains(staf)) {
                        tempo.add(staf);
                        mataPelajaran.setListStaff(tempo);
                        mapelDb.save(mataPelajaran);
                        mapels.add(mataPelajaran);
                        staf.setListMapel(mapels); 
                    }
                }
                    
            } else {
                List<MapelModel> empty = new ArrayList<>();
                staf.setListMapel(empty);
                staf.setTarif(0);
            }
            

            
            staffDb.save(staf);
            response.setStatus(200);
            response.setMessage("Akun berhasil terubah");
            response.setResult(user);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Akun tidak ditemukan");
            response.setResult(null);
        }
        return response;
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

    @PostMapping("/staff/add-image/{username}") 
    public BaseResponse<String> addPengajarImage(@PathVariable String username, @RequestBody MultipartFile file) {
        System.out.println("masuk");
        BaseResponse<String> response = new BaseResponse<>();
        UserModel user = userRestService.getUserByUsername(username);
        StaffModel staff = user.getStaff();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();
        String savedFileName = (dtf.format(now) + "-" + user.getUsername());

        try {
            String url = S3Util.uploadFile(savedFileName, file);
            staff.setUrlFoto(url);
            staffDb.save(staff);
            userDb.save(user);
        } catch (IOException e) {
            staff.setUrlFoto(null);
            staffDb.save(staff);
            userDb.save(user);
        }
        
        return response;
    }

    @GetMapping("/check/{username}")
    public BaseResponse<String> checkUsername(@PathVariable String username) {
        BaseResponse<String> response = new BaseResponse<>();
        UserModel user = userRestService.getUserByUsername(username);
        try {
            response.setStatus(999);
            response.setMessage("not available");
            response.setResult(user.getUsername());
        } catch (NullPointerException e) {
            response.setStatus(200);
            response.setMessage("available");
            response.setResult(null);
        }
        return response;
    }



    public void createUserStaff(StaffDTO staffDTO) {
    }
        
}


