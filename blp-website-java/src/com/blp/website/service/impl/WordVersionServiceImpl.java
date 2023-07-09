package com.blp.website.service.impl;

import com.fy.basejar.tool.ActionToolBase;

import com.blp.website.dao.WordVersionDao;
import com.blp.website.entity.Word;
import com.blp.website.entity.WordVersion;

import com.blp.website.service.WordVersionService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

/**
        * (WordVersionServiceImpl)表服务实现类
        *
        * @author makewanzhghe
        * @since 2022-08-4 10:25:24
        */
@Service
@NoArgsConstructor
public class WordVersionServiceImpl implements WordVersionService {

    @Autowired
    private WordVersionDao wordVersionDao;
    @Override
    public List<WordVersion> getAllWordVersionByWordId(Long wordId) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        return wordVersionDao.getAllWordVersionByWordId(connection,wordId);
    }
}
