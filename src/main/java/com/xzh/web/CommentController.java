package com.xzh.web;

import com.xzh.po.Blog;
import com.xzh.po.Comment;
import com.xzh.po.User;
import com.xzh.service.BlogService;
import com.xzh.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    @Value("${comment.head_portrait}")
    private String head_portrait;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.getCommentsByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Blog blog = blogService.getBlog(comment.getBlog().getId());
        comment.setBlog(blog);
        User user= (User) session.getAttribute("userInfo");
        if(user!=null){
            comment.setHead_portrait(user.getHead_portrait());
            comment.setEmail(user.getEmail());
            comment.setNickname(user.getNickname());
            comment.setAdminComment(true);
        }
        else{
        comment.setHead_portrait(head_portrait);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+comment.getBlog().getId();
    }
}
