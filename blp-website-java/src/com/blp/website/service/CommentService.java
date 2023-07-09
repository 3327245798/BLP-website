package com.blp.website.service;

import com.blp.website.entity.Comment;


import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 17:00:42
 */
public interface CommentService {

    List<Comment> getCommentByPostId(Long postId) throws Exception;

    void insertComment(Comment comment) throws Exception;

    int updateComment(Comment comment) throws Exception;

    int deleteComment(Comment comment) throws Exception;




}
