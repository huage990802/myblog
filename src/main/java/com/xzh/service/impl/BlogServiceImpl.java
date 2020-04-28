package com.xzh.service.impl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xzh.NotFoundException;
import com.xzh.dao.BlogRepository;
import com.xzh.po.Blog;
import com.xzh.po.Tag;
import com.xzh.po.Type;
import com.xzh.service.BlogService;
import com.xzh.service.CommentService;
import com.xzh.util.MarkdownUtils;
import com.xzh.util.MyBeanUtils;
import com.xzh.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    CommentService commentService;


    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        try {
            Blog blog = blogRepository.getOne(id);
            Blog b = new Blog();
            BeanUtils.copyProperties(blog,b);
            b.setContent(MarkdownUtils.markdownToHtmlExtensions(b.getContent()));
            blogRepository.updateViews(id);
            return b;
        } catch (BeansException e) {
            throw new NotFoundException("该博客不存在");
        }
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        if(commentService.getCommentsByBlogId(id)!=null)
        {
            commentService.delete(id);
        }
        blogRepository.deleteById(id);
    }
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if(b==null){
            throw  new NotFoundException();
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));

                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlogByTypeId(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlogByTagId(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join =root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);

    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Pageable pageable= (Pageable) PageRequest.of (0, size, Sort.by(Sort.Direction.DESC,"updateTime"));
        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return  blogRepository.findByQuery(pageable,query);

    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years =blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new LinkedHashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }


}
