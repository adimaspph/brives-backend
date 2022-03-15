package test.bta.brivesc09.restcontroller;

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

@RestController
@RequestMapping("mapel")
public class MapelRestController {

    @Autowired
    private MapelDb mapelDb;

    @GetMapping("/")
    public List<MapelModel> getAllMapel() {
        return mapelDb.findAll();
    }


}
