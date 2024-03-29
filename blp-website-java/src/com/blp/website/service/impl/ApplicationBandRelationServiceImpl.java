package com.blp.website.service.impl;

import com.fy.basejar.tool.ActionToolBase;
import com.blp.website.dao.ApplicationBandRelationDao;
import com.blp.website.entity.ApplicationBandRelation;
import com.blp.website.entity.Band;
import com.blp.website.service.ApplicationBandRelationService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

/**
 * (ApplicationBandRelation)表服务实现类
 *
 * @author makejava
 * @since 2022-02-28 17:02:28
 */
@Service
@NoArgsConstructor
public class ApplicationBandRelationServiceImpl implements ApplicationBandRelationService {

    @Autowired
    ApplicationBandRelationDao applicationBandRelationDao;

    @Override
    public void insertRelation(List<Band> bandList, long id) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        for (Band band : bandList) {
            ApplicationBandRelation applicationBandRelation = new ApplicationBandRelation();
            applicationBandRelation.setBandId(band.getBandId());
            applicationBandRelation.setBandName(band.getBandName());
            applicationBandRelation.setAppCaseId(id);
            applicationBandRelationDao.save(connection, applicationBandRelation);
        }
    }
}
