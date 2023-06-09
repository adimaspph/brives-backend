package test.bta.brivesc09.restcontroller;

import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Sort;
import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.StatusPesananDb;
import test.bta.brivesc09.rest.*;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.PesananRestService;
import test.bta.brivesc09.service.PesananRestServiceImpl;
import test.bta.brivesc09.repository.PesananDb;
import test.bta.brivesc09.repository.UserDb;

//import test.bta.brivesc09.service.StaffRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import test.bta.brivesc09.service.UserRestService;
import test.bta.brivesc09.utils.S3Util;

import java.sql.Timestamp;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import java.time.LocalDateTime;    

@CrossOrigin()

@RestController
@RequestMapping("pesanan")
public class PesananRestController {

    @Autowired
    private PesananDb pesananDb;

    @Autowired
    private JadwalDb jadwalDb;

    @Autowired
    private StatusPesananDb statusPesananDb;

    @Autowired
    private JadwalRestService jadwalRestService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private PesananRestService pesananRestService;

    @GetMapping("/")
    public BaseResponse<List<PesananModel>> getAllPesanan() {
        BaseResponse<List<PesananModel>> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setResult(pesananDb.findAll(Sort.by(Sort.Direction.DESC, "waktuDibuat")));
        return response;
    }

    @GetMapping("/status/{id}")
    public BaseResponse<List<PesananModel>> getPesananByStatus(@PathVariable Long id) {
        BaseResponse<List<PesananModel>> response = new BaseResponse<>();
        try {
            List<PesananModel> data = pesananDb.findByStatus_IdStatusPesanan(id);
            response.setStatus(200);
            response.setMessage("success");
            if (id == 0) {
                response.setResult(pesananDb.findAll());
            } else {
                response.setResult(data);
            }
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @PutMapping("/status/{id}")
    public BaseResponse<PesananModel> updateStatuspesanan(@Valid @PathVariable Long id,
            @RequestBody StatusPesananModel status,
            BindingResult bindingResult) throws ParseException {
        BaseResponse<PesananModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                PesananModel newPesanan = pesananDb.findByIdPesanan(id);
                newPesanan.setStatus(status);
                if (status.getJenisStatus().equals("Terverifikasi")) {
                    JadwalModel jadwal = newPesanan.getJadwal();
                    jadwal.setSiswa(newPesanan.getSiswa());
                    jadwalDb.save(jadwal);
                }
                PesananModel savedJadwal = pesananDb.save(newPesanan);
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

    @GetMapping("/{id}")
    public BaseResponse<PesananModel> getPesananById(@PathVariable Long id) {
        BaseResponse<PesananModel> response = new BaseResponse<>();
        try {
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(pesananDb.findByIdPesanan(id));
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @GetMapping("/siswa/{id}")
    public BaseResponse<List<PesananModel>> getPesananBySiswa(@PathVariable Long id) {
        BaseResponse<List<PesananModel>> response = new BaseResponse<>();
        try {
            List<PesananModel> data = pesananDb.findBySiswa_IdSiswa(id);
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

    @GetMapping("/siswa/{idSiswa}/status/{idStatus}")
    public BaseResponse<List<PesananModel>> getPesananByStatusSiswa(@PathVariable Long idSiswa,
            @PathVariable Long idStatus) {
        BaseResponse<List<PesananModel>> response = new BaseResponse<>();
        try {
            List<PesananModel> datasiswa = pesananDb.findBySiswa_IdSiswa(idSiswa);
            ArrayList<PesananModel> pesanan = new ArrayList<PesananModel>();
            for (PesananModel x : datasiswa) {
                if (x.getStatus().getIdStatusPesanan().equals(idStatus)) {
                    pesanan.add(x);
                }
            }
            response.setStatus(200);
            response.setMessage("success");

            if (idStatus == 0) {
                response.setResult(pesananDb.findBySiswa_IdSiswa(idSiswa));
            } else {
                response.setResult(pesanan);
            }

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }

    @PostMapping()
    public BaseResponse<PesananModel> createPesanan(
            HttpServletRequest request,
            @Valid @RequestBody PesananRest pesananRest,
            BindingResult bindingResult) {
        BaseResponse<PesananModel> response = new BaseResponse<>();

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            try {
                PesananModel pesanan = new PesananModel();
                UserModel authUser = userRestService.getUserFromJwt(request);

                pesanan.setStatus(statusPesananDb.findByIdStatusPesanan(1L));
                pesanan.setNominal(pesananRest.nominal);
                pesanan.setMateri(pesananRest.materi);
                pesanan.setWaktuDibuat(LocalDateTime.now());
                JadwalModel jadwal = jadwalRestService.getJadwalById(pesananRest.idJadwal);
                pesanan.setJadwal(jadwal);
                pesanan.setSiswa(authUser.getSiswa());

                pesananRestService.createPesanan(pesanan);
                response.setResult(pesanan);

            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.toString());
            }
            return response;
        }
    }

    @PutMapping("/bayar/{id}")
    public BaseResponse<PesananModel> addPembayaran(@Valid @PathVariable Long id,
            @RequestParam("file") MultipartFile file, @RequestParam("metodePembayaran") String metodePembayaran) {
        // @RequestBody PesananModel pesanan,
        BaseResponse<PesananModel> response = new BaseResponse<>();
        try {
            PesananModel newPesanan = pesananDb.findByIdPesanan(id);
            String fileName = file.getOriginalFilename();
            String namaSiswa = newPesanan.getSiswa().getUser().getNamaLengkap().replace(' ', '-');
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
            LocalDateTime now = LocalDateTime.now();  
            
            String savedFileName = (dtf.format(now) + "-" + namaSiswa + "-" + fileName);
            System.out.println("savedFileName: " + savedFileName);

            String url = S3Util.uploadFile(savedFileName, file);

            newPesanan.setBuktiBayar(url);
            newPesanan.setMetodePembayaran(metodePembayaran);

            PesananModel savedPesanan = pesananDb.save(newPesanan);
            pesananDb.save(savedPesanan);
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(savedPesanan);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.toString());
        }
        return response;

    }

    @PutMapping("/addAlasan/{id}")
    public BaseResponse<PesananModel> addAlasanPenolakan(@Valid @PathVariable Long id,
            @RequestBody PesananModel pesanan,
            BindingResult bindingResult) throws ParseException {
        BaseResponse<PesananModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                PesananModel newPesanan = pesananDb.findByIdPesanan(id);
                newPesanan.setAlasan(pesanan.getAlasan());
                PesananModel savedPesanan = pesananDb.save(newPesanan);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(savedPesanan);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

    @GetMapping("/allTransaction/{year}")
    public BaseResponse<List<HashMap<String, String>>> getPesananByTahun(@PathVariable String year) {
        BaseResponse<List<HashMap<String, String>>> response = new BaseResponse<>();
        try {
            List<HashMap<String, String>> allTrans = pesananRestService.getAllTransactionPerYear(year);
            response.setStatus(200);
            response.setMessage("berhasil");
            response.setResult(allTrans);

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }

    @GetMapping("/allPrivat/{year}")
    public BaseResponse<List<HashMap<String, String>>> getKelasPrivatByTahun(@PathVariable String year) {
        BaseResponse<List<HashMap<String, String>>> response = new BaseResponse<>();
        try {
            List<HashMap<String, String>> allTrans = pesananRestService.getAllKelasPrivatPerYear(year);
            response.setStatus(200);
            response.setMessage("berhasil");
            response.setResult(allTrans);

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }

    @GetMapping("/allTambahan/{year}")
    public BaseResponse<List<HashMap<String, String>>> getKelasTambahanByTahun(@PathVariable String year) {
        BaseResponse<List<HashMap<String, String>>> response = new BaseResponse<>();
        try {
            List<HashMap<String, String>> allTrans = jadwalRestService.getAllKelasTambahanPerYear(year);
            response.setStatus(200);
            response.setMessage("berhasil");
            response.setResult(allTrans);

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }

    @GetMapping("/jadwal/{idJadwal}/status/{idStatus}")
    public BaseResponse<PesananModel> getJadwalStatusUnique(@PathVariable Long idJadwal, @PathVariable Long idStatus) {
        BaseResponse<PesananModel> response = new BaseResponse<>();
        try {
            List<PesananModel> datasiswa = pesananDb.findByJadwal_IdJadwal_AndStatus_IdStatusPesanan(idJadwal,
                    idStatus);

            response.setStatus(200);
            response.setMessage("success");
            response.setResult(datasiswa.get(0));

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }

        return response;
    }

}
