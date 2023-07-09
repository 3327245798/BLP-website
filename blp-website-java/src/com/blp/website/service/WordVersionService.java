package com.blp.website.service;

import com.blp.website.entity.Word;
import com.blp.website.entity.WordVersion;

import java.util.List;

/**
 * (Word)表服务接口
 *
 * @author makewanzghe
 * @since 2022-8-4
 */
public interface WordVersionService {
    /**
     * 获取所有词汇的版本记录
     * @return
     *
     */

    List<WordVersion> getAllWordVersionByWordId(Long wordId)throws Exception;




}