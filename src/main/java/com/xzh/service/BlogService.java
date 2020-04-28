package com.xzh.service;

import com.xzh.po.Blog;
import com.xzh.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getAndConvert(Long id);

    void deleteBlog(Long id);

    Blog getBlog(Long id);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    Page<Blog> listBlog (Pageable pageable, BlogQuery blog);

    Page<Blog> listBlogByTypeId (Pageable pageable, BlogQuery blog);

    Page<Blog> listBlogByTagId (Pageable pageable, Long tagId);

    Page<Blog> listBlog (Pageable pageable);

    List<Blog> listBlogTop(Integer size);

    Page<Blog> listBlog(Pageable pageable,String query);

    Long countBlog();

    Map<String,List<Blog>> archiveBlog();




}
