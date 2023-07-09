package com.blp.website.dao.impl;

import com.blp.website.dao.ApplicationBandRelationDao;
import com.blp.website.entity.ApplicationBandRelation;
import com.fy.toolhelper.db.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationBandRelationDaoImpl extends BaseDaoImpl<ApplicationBandRelation> implements ApplicationBandRelationDao {
    public ApplicationBandRelationDaoImpl() throws Exception {
    }
}
