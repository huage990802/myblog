package com.xzh.service;

import com.xzh.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByBlogId(Long blogID);

    Comment saveComment(Comment comment);

    void delete(Long blogId);
}
