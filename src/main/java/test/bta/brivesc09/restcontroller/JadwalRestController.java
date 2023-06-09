package test.bta.brivesc09.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.LogDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.rest.PesanJadwalModelRest;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private LogRestController logRestController;

    @Autowired
    private JadwalDb jadwalDb;

    @Autowired
    private LogDb logDb;

    @GetMapping("/hari/{username}")
    public BaseResponse<List<JadwalResponse>> getAllJadwalByTanggal(
            @PathVariable String username,
            @RequestParam Integer tanggal,
            @RequestParam Integer bulan,
            @RequestParam Integer tahun
    ) {
        BaseResponse<List<JadwalResponse>> response = new BaseResponse<>();
        if (tanggal == null || bulan == null || tahun == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            try {
                UserModel authUser = userRestService.getUserByUsername(username);
                List<JadwalResponse> result = new ArrayList<>();
                List<JadwalModel> listJadwal = jadwalRestService.getListJadwalByTanggal(LocalDate.of(tahun, bulan, tanggal), authUser.getStaff());

                for (JadwalModel jadwal :
                        listJadwal) {
                    JadwalResponse temp = new JadwalResponse();
                    temp.jadwal = jadwal;

                    if (jadwal.getSiswa() != null) {
                        temp.siswa = jadwal.getSiswa().getUser();
                        temp.statusPesanan = "Terpesan";
//                        System.out.println(jadwal.getIdJadwal());
//                        System.out.println(jadwalRestService.getJadwalById(jadwal.getIdJadwal()));
//                        System.out.println(jadwalRestService.getVerifiedPesanan(jadwal.getIdJadwal()));
                        temp.materi = jadwalRestService.getVerifiedPesanan(jadwal.getIdJadwal()).getMateri();
                    }
                    result.add(temp);
                }

                response.setResult(result);

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
            @Valid @RequestBody JadwalRest jadwalRest,
            BindingResult bindingResult
    ) {
        System.out.println(jadwalRest.username);
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        UserModel authUser = userRestService.getUserByUsername(jadwalRest.username);
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

    @PostMapping("/kelas-tambahan")
    public BaseResponse<JadwalModel> createKelasTambahan(
            @Valid @RequestBody JadwalRest jadwalRest,
            BindingResult bindingResult
    ) {
//        System.out.println(jadwalRest.username);
        BaseResponse<JadwalModel> response = new BaseResponse<>();
        UserModel authUser = userRestService.getUserByUsername(jadwalRest.username);
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
                jadwal.setLinkZoom(jadwalRest.link);

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


                System.out.println(newJadwal.getIdJadwal());
                LogModel newLog = new LogModel();
                newLog.setJadwal(newJadwal);
                newLog.setCatatan("");
                newLog.setStaff(jadwal.getStaff());
                newLog.setStatusKehadiran("KOSONG");
                logDb.save(newLog);

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

    @GetMapping("/mapel")
    public BaseResponse<List<PesanJadwalModelRest>> getJadwalByMapelAndDate(
            @RequestParam Integer tanggal,
            @RequestParam Integer bulan,
            @RequestParam Integer tahun,
            @RequestParam Long idMapel
    ) {
        BaseResponse<List<PesanJadwalModelRest>> response = new BaseResponse<>();
        if (tanggal == null || bulan == null || tahun == null || idMapel == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        }
        else {
            try {
                List<PesanJadwalModelRest> result = new ArrayList<>();
                List<JadwalModel> listJadwal = jadwalRestService.getAllJadwalByIdMapel(idMapel, LocalDate.of(tahun, bulan, tanggal));
                for (JadwalModel jadwal : listJadwal) {
                    PesanJadwalModelRest temp = new PesanJadwalModelRest();
                    temp.jadwal = jadwal;
                    temp.nama = jadwal.getStaff().getUser().getNamaLengkap();
                    temp.tarif = jadwal.getStaff().getTarif();
                    temp.username = jadwal.getStaff().getUser().getUsername();
                    result.add(temp);
                }
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(result);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

//    @GetMapping("/pengajar/{id}")
//    public BaseResponse<PesanJadwalModelRest> getStaffByIdJadwal(@PathVariable Long id) {
//        BaseResponse<PesanJadwalModelRest> response = new BaseResponse<>();
//        try {
//            JadwalModel jadwal = jadwalRestService.getJadwalById(id);
//            PesanJadwalModelRest result = new PesanJadwalModelRest();
//            result.tarif = jadwal.getStaff().getTarif();
//            result.nama = jadwal.getStaff().getUser().getNamaLengkap();
//            result.username = jadwal.getStaff().getUser().getUsername();
//            response.setStatus(200);
//            response.setMessage("success");
//            response.setResult(result);
//        } catch (Exception e) {
//            response.setStatus(400);
//            response.setMessage(e.toString());
//            response.setResult(null);
//        }
//        return response;
//    }
}
