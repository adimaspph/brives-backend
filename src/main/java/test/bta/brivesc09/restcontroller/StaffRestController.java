package test.bta.brivesc09.restcontroller;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.repository.StaffDb;
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
@RequestMapping("/api/v1/staf")
public class StaffRestController {
    @Autowired
    private StaffRestService staffRestService;

    @GetMapping("/{id}")
    public BaseResponse<StaffModel> getStaffById(@PathVariable Long id) {
        BaseResponse<StaffModel> response = new BaseResponse<>();
        try{
            StaffModel staff = staffRestService.getStaffByIdStaff(id);
            if (staff != null){
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(staff);
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



