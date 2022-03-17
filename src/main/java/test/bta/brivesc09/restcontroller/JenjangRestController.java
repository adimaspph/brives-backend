package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.JenjangModel;
import test.bta.brivesc09.repository.JenjangDb;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("jenjang")
public class JenjangRestController {

    @Autowired
    private JenjangDb jenjangDb;

    @GetMapping("/")
    public List<JenjangModel> getAllJenjang() {
        return jenjangDb.findAll();
    }

    // Add mapel
    @PostMapping("/")
    public JenjangModel createJenjang (@RequestBody JenjangModel jenjang) {
        return jenjangDb.save(jenjang);
    }


}
