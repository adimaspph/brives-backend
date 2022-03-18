package test.bta.brivesc09.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.JenisKelas;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("jadwal")
public class JadwalRestController {

    @Autowired
    private JadwalRestService jadwalRestService;

    @Autowired
    private MapelRestService mapelRestService;

    @Autowired
    private StaffRestService staffRestService;

    @GetMapping("/")
    public List<JadwalModel> getAllJadwal() {
        return jadwalRestService.getAllJadwal();
    }

    @GetMapping("/{tanggal}")
    public List<JadwalModel> getAllJadwalByTanggal(@PathVariable LocalDate tanggal) {
        return jadwalRestService.getListJadwalByTanggal(tanggal);
    }

    // Create jadwal
    @PostMapping("/")
    public BaseResponse<JadwalModel> createJadwal (
            @Valid @RequestBody JadwalRest jadwalrest,
            BindingResult bindingResult
    ) {
//        System.out.println(jadwalrest.tahun + jadwalrest.bulan + jadwalrest.tanggal + jadwalrest.jam + jadwalrest.menit);

//        int jam, menit, tahun, bulan, tanggal;
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missong field"
            );
        } else {
            try{
                LocalTime start = LocalTime.of(jadwalrest.jam, jadwalrest.menit);
                JadwalModel jadwal = new JadwalModel();
                jadwal.setTanggal(LocalDate.of(jadwalrest.tahun, jadwalrest.bulan, jadwalrest.tanggal));
                jadwal.setWaktuMulai(start);

                jadwal.setMapel(mapelRestService.getMapelById((long) 1));
                jadwal.setJenisKelas(JenisKelas.PRIVATE);
                jadwal.setStaff(staffRestService.getStaffByIdStaff((long) 1));

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
