package test.bta.brivesc09.restcontroller;

import nonapi.io.github.classgraph.json.JSONUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.service.PesananRestServiceImpl;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.model.UserModel;
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
import java.text.ParseException;
import javax.validation.Valid;
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
    private PesananRestServiceImpl pesananService;

    @GetMapping("/")
    public BaseResponse<List<PesananModel>> getAllPesanan() {
        BaseResponse<List<PesananModel>> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setResult(pesananDb.findAll());

        return response;
    }


}
