package test.bta.brivesc09.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import test.bta.brivesc09.model.*;
import test.bta.brivesc09.repository.JadwalDb;
import test.bta.brivesc09.repository.LogDb;
import test.bta.brivesc09.repository.MapelDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.rest.JadwalRest;
import test.bta.brivesc09.rest.PesanJadwalModelRest;
import test.bta.brivesc09.service.*;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("log")
public class LogRestController {

    @Autowired
    private JadwalRestService jadwalRestService;

    @Autowired
    private MapelRestService mapelRestService;

    @Autowired
    private StaffRestService staffRestService;

    @Autowired
    private UserRestService userRestService;

    @Autowired
    private LogRestService logRestService;

    @Autowired
    private LogDb logDb;

    @GetMapping("/")
    public BaseResponse<List<LogModel>> getAllLog() {
        BaseResponse<List<LogModel>> response = new BaseResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setResult(logDb.findAll());

        return response;
    }

    @RequestMapping("/")
    public BaseResponse<LogModel> createLog(@Valid @RequestBody LogModel log, BindingResult bindingResult)
            throws ParseException {
        BaseResponse<LogModel> response = new BaseResponse<>();
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Body has invalid type or missing field");
        } else {
            try {
                LogModel newLog = new LogModel();
                newLog.setIdLog(log.getIdLog());
                newLog.setJadwal(log.getJadwal());
                newLog.setCatatan(log.getCatatan());
                newLog.setStaff(log.getStaff());
                newLog.setStatusKehadiran("Dibuat");

                LogModel saveLog = logDb.save(log);
                response.setStatus(200);
                response.setMessage("success");
                response.setResult(saveLog);
            } catch (Exception e) {
                response.setStatus(400);
                response.setMessage(e.toString());
                response.setResult(null);
            }
            return response;
        }
    }


}
