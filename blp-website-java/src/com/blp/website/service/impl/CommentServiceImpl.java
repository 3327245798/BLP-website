package com.blp.website.service.impl;

import com.blp.website.dao.CommentDao;
import com.blp.website.dao.PostDao;
import com.blp.website.entity.Comment;
import com.blp.website.entity.Post;
import com.blp.website.service.CommentService;
import com.blp.website.service.PostService;
import com.fy.basejar.tool.ActionToolBase;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

/**
 * (CommentServiceImpl)表服务实现类
 *
 * @author makewz
 * @since 2022-10-17 17:00:42
 */
@Service
@NoArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Override
    public List<Comment> getCommentByPostId(Long postId) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        return commentDao.getCommentsByPostId(connection,postId);

    }

    @Override
    public void insertComment(Comment comment) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        commentDao.save(connection, comment);
    }

    @Override
    public int updateComment(Comment comment) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        int i=(int)commentDao.updateIgnoreNull(connection,comment);
        return i;
    }

    @Override
    public int deleteComment(Comment comment) throws Exception {
        final Connection connection = ActionToolBase.getDBConnection();
        int i=(int)commentDao.delete(connection,comment);
        return i;
    }
}
