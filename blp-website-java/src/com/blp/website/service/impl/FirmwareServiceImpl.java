package com.blp.website.service.impl;

import com.blp.website.dao.FirmwareDao;
import com.blp.website.dao.NewParamDao;
import com.blp.website.dao.UnitDao;
import com.blp.website.entity.Firmware;
import com.blp.website.entity.NewParam;
import com.blp.website.entity.Unit;
import com.blp.website.service.FirmwareService;
import com.blp.website.service.UnitService;
import com.fy.basejar.tool.ActionToolBase;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * (Unit)表服务实现类
 *
 * @author makejava
 * @since 2022-05-06 20:34:27
 */
@Service
@NoArgsConstructor
public class FirmwareServiceImpl implements FirmwareService {

    @Autowired
    private FirmwareDao firmwareDao;

    @Autowired
    private NewParamDao newParamDao;

    @Override
    public List<Firmware> getAllFirmwares() throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        return firmwareDao.getAllFirmwares(connection);

    }

    @Override
    public List<Firmware> getFirmwaresByToolId(Long toolId) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        return firmwareDao.getFirmwaresByToolId(connection,toolId);

    }

    @Override
    public Firmware getToolInfoFirmwareInfoParamInfoByWordId(Long wordId) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        Firmware firmware = firmwareDao.getFirmwareInfoByWordId(connection, wordId);
        if(firmware != null) {
            List<NewParam> params = newParamDao.getToolInfoFirmwareInfoParamInfoByWordId(connection, wordId);
            List<NewParam> inputParams = new ArrayList<>();
            List<NewParam> outputParams = new ArrayList<>();
            for (NewParam param : params) {
                if (param.getInOutType() == 1) {
                    inputParams.add(param);
                } else {
                    outputParams.add(param);
                }
            }

            firmware.setInputParams(inputParams);
            firmware.setOutputParams(outputParams);
        }
        return firmware;
    }

    @Override
    public int updateFirmware(Firmware firmware) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        int i= (int) firmwareDao.updateIgnoreNull(connection,firmware);
        return i;
    }

    @Override
    public void insertFirmware(Firmware firmware) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        firmwareDao.save(connection,firmware);
    }

    @Override
    public int deleteFirmware(Firmware firmware) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        int i= (int) firmwareDao.delete(connection,firmware);
        return i;
    }
}