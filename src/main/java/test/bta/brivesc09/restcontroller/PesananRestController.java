package test.bta.brivesc09.restcontroller;

import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Sort;
import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.StatusPesananDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.rest.PesananRest;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.PesananRestServiceImpl;
import test.bta.brivesc09.repository.PesananDb;
import test.bta.brivesc09.repository.UserDb;

//import test.bta.brivesc09.service.StaffRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import test.bta.brivesc09.service.UserRestService;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "https://brives-staging.herokuapp.com")

@RestController
@RequestMapping("pesanan")
public class PesananRestController {

    @Autowired
    private PesananDb pesananDb;

    @Autowired
    private UserDb userDb;

    @Autowired
    private StatusPesananDb statusPesananDb;

    @Autowired
    private JadwalRestService jadwalRestService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private PesananRestServiceImpl pesananService;

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
    public BaseResponse<PesananModel> updateStatuspesanan(@Valid @PathVariable Long id, @RequestBody StatusPesananModel status,
                                                 BindingResult bindingResult) throws ParseException {
        BaseResponse<PesananModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                PesananModel newPesanan = pesananDb.findByIdPesanan(id);
                newPesanan.setStatus(status);
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

    @PostMapping()
    public BaseResponse<PesananModel> createJadwal(
            HttpServletRequest request,
            @Valid @RequestBody PesananRest pesananRest,
            BindingResult bindingResult
    ) {
        BaseResponse<PesananModel> response = new BaseResponse<>();

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
            );
        } else {
            try {
                PesananModel pesanan = new PesananModel();
                UserModel authUser = userRestService.getUserFromJwt(request);

                pesanan.setStatus(statusPesananDb.findByIdStatusPesanan(1L));
                pesanan.setNominal(pesananRest.nominal);
                pesanan.setMateri(pesananRest.materi);
                pesanan.setWaktuDibuat(LocalDateTime.now());
                pesanan.setJadwal(jadwalRestService.getJadwalById(pesananRest.idJadwal));
                pesanan.setSiswa(authUser.getSiswa());

                response.setResult(pesanan);

            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.toString());
            }
            return response;
        }
    }

}