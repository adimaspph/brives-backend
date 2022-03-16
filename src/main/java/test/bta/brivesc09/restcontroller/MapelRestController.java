package test.bta.brivesc09.restcontroller;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.service.StaffRestService;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("mapel")
public class MapelRestController {

    @Autowired
    private MapelDb mapelDb;

    @GetMapping("/")
    public List<MapelModel> getAllMapel() {
        return mapelDb.findAll();
    }

    // Add mapel
    @PostMapping("/")
    public MapelModel createMapel (@RequestBody MapelModel mapel) {
        return mapelDb.save(mapel);
    }

    // Get Mapel by Id
    @GetMapping("/{id}")
    public ResponseEntity <MapelModel> getMapelById(@PathVariable Long id) {
        MapelModel mapel = mapelDb.findByIdMapel(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapel tidak ditemukan"));
        return ResponseEntity.ok(mapel);
    }

    // Update mapel
    @PutMapping("/{id}")
    public ResponseEntity<MapelModel> updateMapel(@PathVariable Long id, @RequestBody MapelModel mapelDetails) {
        MapelModel mapel = mapelDb.findByIdMapel(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapel tidak ditemukan"));
        mapel.setNamaMapel(mapelDetails.getNamaMapel());
        mapel.setDeskripsi(mapelDetails.getDeskripsi());
        mapel.setJenjang(mapelDetails.getJenjang());

        MapelModel updatedMapel = mapelDb.save(mapel);
        return ResponseEntity.ok(updatedMapel);
    }


}
