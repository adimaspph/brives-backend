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
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import java.text.ParseException;
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

    @Autowired
    private JadwalDb jadwalDb;

    @GetMapping()
    public BaseResponse<List<JadwalModel>> getAllJadwalByTanggal(
            HttpServletRequest request,
            @RequestParam Integer tanggal,
            @RequestParam Integer bulan,
            @RequestParam Integer tahun
    ) {
        BaseResponse<List<JadwalModel>> response = new BaseResponse<>();
        if (tanggal == null || bulan == null || tahun == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            try {
                UserModel authUser = userRestService.getUserFromJwt(request);
                response.setResult(jadwalRestService.getListJadwalByTanggal(LocalDate.of(tahun, bulan, tanggal), authUser.getStaff()));

            } catch (NullPointerException e) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not authenticated", e);

            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.toString());
            }
            return response;
        }
    }

    // Create jadwal
    @PostMapping()
    public BaseResponse<JadwalModel> createJadwal(
            HttpServletRequest request,
            @Valid @RequestBody JadwalRest jadwalRest,
            BindingResult bindingResult
    ) {
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        UserModel authUser = userRestService.getUserFromJwt(request);
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            try {
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
                throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);

            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage());
            }
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public BaseResponse<JadwalModel> deleteJadwalbyId(@PathVariable Long id) {
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        try {
            Boolean result = jadwalRestService.deleteJadwalById(id);
            if (result == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Gagal delete");
            }
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(null);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<JadwalModel> getJadwalById(@PathVariable Long id) {
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        try {
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(jadwalDb.findByIdJadwal(id));
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @PutMapping("/addLink/{id}")
    public BaseResponse<JadwalModel> addLinkZoom(@Valid @PathVariable Long id, @RequestBody JadwalModel jadwal,
                                                 BindingResult bindingResult) throws ParseException {
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                JadwalModel newJadwal = jadwalDb.findByIdJadwal(id);
                newJadwal.setLinkZoom(jadwal.getLinkZoom());
                JadwalModel savedJadwal = jadwalDb.save(newJadwal);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(savedJadwal);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

    @GetMapping("/mapel/")
    public BaseResponse<List<JadwalModel>> getJadwalByMapelAndDate(
            @RequestParam Integer tanggal,
            @RequestParam Integer bulan,
            @RequestParam Integer tahun,
            @RequestParam Long idMapel
    ) {
        BaseResponse<List<JadwalModel>> response = new BaseResponse<>();
        if (tanggal == null || bulan == null || tahun == null || idMapel == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        }
        else {
            try {
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(jadwalRestService.getAllJadwalByIdMapel(idMapel, LocalDate.of(tahun, bulan, tanggal)));
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

}
