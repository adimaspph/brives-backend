package test.bta.brivesc09.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "https://brives-staging.herokuapp.com")
@RestController
@RequestMapping("jadwal")
public class JadwalRestController {

    @Autowired
    private JadwalRestService jadwalRestService;

    @Autowired
    private MapelRestService mapelRestService;

    @Autowired
    private StaffRestService staffRestService;

    @Autowired
    private UserRestService userRestService;

//    @GetMapping()
//    public List<JadwalModel> getAllJadwal() {
//        return jadwalRestService.getAllJadwal();
//    }

    @GetMapping()
    public BaseResponse<List<JadwalModel>> getAllJadwalByTanggal(
            HttpServletRequest request,
            @RequestParam Integer tanggal,
            @RequestParam Integer bulan,
            @RequestParam Integer tahun
        ) {
        BaseResponse<List<JadwalModel>> response = new BaseResponse<>();
        UserModel authUser = userRestService.getUserFromJwt(request);
        response.setResult(jadwalRestService.getListJadwalByTanggal(LocalDate.of(tahun, bulan, tanggal), authUser.getStaff()));
        return response;
    }

    // Create jadwal
    @PostMapping()
    public BaseResponse<JadwalModel> createJadwal (
            HttpServletRequest request,
            @Valid @RequestBody JadwalRest jadwalRest,
            BindingResult bindingResult
    ) {
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        UserModel authUser = userRestService.getUserFromJwt(request);
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missong field"
            );
        } else {
            try{
                LocalTime start = LocalTime.of(jadwalRest.jam, jadwalRest.menit);
                JadwalModel jadwal = new JadwalModel();
                jadwal.setTanggal(LocalDate.of(jadwalRest.tahun, jadwalRest.bulan, jadwalRest.tanggal));
                jadwal.setWaktuMulai(start);

                jadwal.setMapel(mapelRestService.getMapelById(jadwalRest.mapel));
                jadwal.setJenisKelas(jadwalRest.jenisKelas);
                jadwal.setStaff(authUser.getStaff());

                JadwalModel newJadwal = jadwalRestService.createJadwal(jadwal);

                response.setStatus(200);
                response.setMessage("success");
                response.setResult(newJadwal);

            } catch (UnsupportedOperationException e) {
//                response.setStatus(400);
//                response.setMessage("Jadwal bertabrakan");
//                response.setResult(null);
                throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE, "Jadwal bertabrakan", e);

            } catch (Exception e) {
//                response.setStatus(500);
//                response.setMessage(e.toString());
//                response.setResult(null);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.toString());
            }
            return response;
        }



//        return null;
    }
}
