package test.bta.brivesc09.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.JenisKelas;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.service.JadwalRestService;
import test.bta.brivesc09.service.MapelRestService;

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
    private MapelDb mapelDb;

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
    public JadwalModel createJadwal (
//            @RequestBody Integer tahun,
//            @RequestBody Integer bulan,
//            @RequestBody Integer tanggal,
//            @RequestBody Integer jam,
//            @RequestBody Integer menit
//            @Valid @RequestBody JadwalModel jadwal,
//            BindingResult bindingResult
    ) {
//        System.out.println(tahun + bulan + tanggal + jam + menit);

//        int jam, menit, tahun, bulan, tanggal;

        LocalTime start = LocalTime.of(16, 30);
        JadwalModel jadwal = new JadwalModel();
        jadwal.setTanggal(LocalDate.of(2022, 3, 20));
        jadwal.setWaktuMulai(start);
        jadwal.setWaktuSelesai(start.plusMinutes(90));


        jadwal.setMapel(mapelRestService.getMapelById((long) 1));
        jadwal.setJenisKelas(JenisKelas.PRIVATE);

//        if (bindingResult.hasErrors()) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missong field"
//            );
//        } else {
            return jadwalRestService.createJadwal(jadwal);
//        }



//        return null;
    }
}
