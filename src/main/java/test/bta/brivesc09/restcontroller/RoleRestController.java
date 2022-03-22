package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.service.MapelRestService;
import test.bta.brivesc09.service.StaffRestService;
import test.bta.brivesc09.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.text.ParseException;
import java.util.*;

import javax.persistence.EmbeddedId;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@CrossOrigin(origins = "https://brives-staging.herokuapp.com")
@RestController
@RequestMapping("/role")
public class RoleRestController {
    @Autowired
    private UserRestService userRestService;

    @Autowired
    private StaffRestService staffRestService;

    @Autowired
    private RoleDb roleDb;

    @GetMapping("/all")
    public BaseResponse<List<RoleModel>> getAllRole() {
        BaseResponse<List<RoleModel>> response = new BaseResponse<>();

        response.setStatus(200);
        response.setMessage("success");
        response.setResult(roleDb.findAll());

        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<RoleModel> getRoleById(@PathVariable Long id) {
        BaseResponse<RoleModel> response = new BaseResponse<>();
        try{
            if (roleDb.findAllByIdRole(id) != null){
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(roleDb.findAllByIdRole(id));
            } else {
                response.setStatus(400);
                response.setMessage("null");
                response.setResult(null);
            }

        } catch (Exception e) {
            response.setStatus(400);
            response.setMessage(e.toString());
            response.setResult(null);
        }
        return response;
    }




}


