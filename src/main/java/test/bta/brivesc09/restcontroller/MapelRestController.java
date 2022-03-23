package test.bta.brivesc09.restcontroller;

import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.service.MapelRestServiceImpl;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.repository.MapelDb;
//import test.bta.brivesc09.service.StaffRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "https://brives-staging.herokuapp.com")

@RestController
@RequestMapping("mapel")
public class MapelRestController {

    @Autowired
    private MapelDb mapelDb;

    @Autowired
    private MapelRestServiceImpl mapelService;

    @GetMapping("/")
    public BaseResponse<List<MapelModel>> getAllMapel() {
        BaseResponse<List<MapelModel>> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setResult(mapelDb.findAll());

        return response;
    }

    @GetMapping("/nama/{namaMapel}")
    public BaseResponse<List<MapelModel>> getAllMapelByNamaMapel(@PathVariable String namaMapel) {
        BaseResponse<List<MapelModel>> response = new BaseResponse<>();
        response.setResult(mapelDb.findAllByNamaMapel(namaMapel));
        if (response.getResult().size() > 0) {
            response.setStatus(400);
            response.setMessage("Mata Kuliah Sudah Ada");
            response.setResult(null);
        } else {
            response.setStatus(200);
            response.setMessage("success");
        }

        return response;
    }

    @RequestMapping("/")
    public BaseResponse<MapelModel> createMapel(@Valid @RequestBody MapelModel mapel, BindingResult bindingResult)
            throws ParseException {
        BaseResponse<MapelModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                MapelModel newMapel = new MapelModel();
                newMapel.setIdMapel(mapel.getIdMapel());
                newMapel.setNamaMapel(mapel.getNamaMapel());
                newMapel.setListJenjang(mapel.getListJenjang());
                newMapel.setDeskripsi(mapel.getDeskripsi());
                newMapel.setListStaff(mapel.getListStaff());
                newMapel.setListJadwal(mapel.getListJadwal());

                MapelModel savedMapel = mapelDb.save(mapel);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(savedMapel);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

    @GetMapping("/{id}")
    public BaseResponse<MapelModel> getMapelById(@PathVariable Long id) {
        BaseResponse<MapelModel> response = new BaseResponse<>();
        try {
            response.setStatus(200);
            response.setMessage("success");
            response.setResult(mapelDb.findByIdMapel(id));
        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

    @PutMapping("/{id}")
    public BaseResponse<MapelModel> updateMapel(@Valid @PathVariable Long id, @RequestBody MapelModel mapel,
            BindingResult bindingResult) throws ParseException {
        BaseResponse<MapelModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                MapelModel newMapel = mapelDb.findByIdMapel(id);
                newMapel.setNamaMapel(mapel.getNamaMapel());
                newMapel.setListJenjang(mapel.getListJenjang());
                newMapel.setDeskripsi(mapel.getDeskripsi());
                newMapel.setListStaff(mapel.getListStaff());
                newMapel.setListJadwal(mapel.getListJadwal());

                MapelModel savedMapel = mapelDb.save(newMapel);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(savedMapel);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }

    @DeleteMapping("/{id}")
    public BaseResponse<MapelModel> deleteMapelbyId(@PathVariable Long id) {
        BaseResponse<MapelModel> response = new BaseResponse<>();
        try {
            MapelModel mapel = mapelDb.findByIdMapel(id);
            mapelDb.delete(mapel);

            response.setStatus(200);
            response.setMessage("success");
            response.setResult(null);

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }

}
