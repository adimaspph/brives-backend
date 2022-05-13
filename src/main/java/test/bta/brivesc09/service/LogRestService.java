package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.model.LogModel;

import java.time.LocalDate;
import java.util.List;

public interface LogRestService {
//    List<LogModel> getAllLog();
    LogModel getLogById(Long id);
    LogModel createLog(LogModel log);
}
