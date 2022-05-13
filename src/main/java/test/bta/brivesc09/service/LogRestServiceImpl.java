package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.LogModel;
import test.bta.brivesc09.model.MapelModel;
import test.bta.brivesc09.model.PesananModel;
import test.bta.brivesc09.repository.LogDb;
import test.bta.brivesc09.repository.MapelDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.bta.brivesc09.repository.PesananDb;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogRestServiceImpl implements LogRestService {
    @Autowired
    private LogDb logDb;

    @Override
    public LogModel createLog(LogModel log) {
        return logDb.save(log);
    }

    @Override
    public LogModel getLogById(Long id) {
        return logDb.getById(id);
    }


}
