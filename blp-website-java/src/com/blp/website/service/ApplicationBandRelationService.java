package com.blp.website.service;

import com.blp.website.entity.Band;

import java.util.List;

/**
 * (ApplicationBandRelation)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 17:02:27
 */
public interface ApplicationBandRelationService {

    void insertRelation(List<Band> bandList, long id) throws Exception;
}
