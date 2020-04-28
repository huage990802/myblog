package com.xzh.web.admin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xzh.po.Blog;
import com.xzh.po.User;
import com.xzh.service.BlogService;
import com.xzh.service.TagService;
import com.xzh.service.TypeService;
import com.xzh.service.impl.BlogServiceImpl;
import com.xzh.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    @RequestMapping("/blogs")
    public String toBlogs (@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageablem, BlogQuery blog, Model model){
        Page<Blog> blogs = blogService.listBlog(pageablem, blog);
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogs);
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String serach(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageablem, BlogQuery blog, Model model){
        Page<Blog> blogs = blogService.listBlog(pageablem, blog);
        model.addAttribute("page",blogs);
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("blog",new Blog());
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(Model model,@PathVariable Long id){
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        return "admin/blogs-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("userInfo");
        blog.setUser(user);
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        if(blog.getFlag()==null||"".equals(blog.getFlag())){
            blog.setFlag("原创");
        }
        Blog b=null;
        if(blog.getId()==null){
            b = blogService.saveBlog(blog);
        }
        else{
            b= blogService.updateBlog(blog.getId(), blog);
        }

        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }
        else {
                attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable("id") Long id,RedirectAttributes attributes){
        String name = blogService.getBlog(id).getTitle();
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除博客"+name+"成功！");
        return "redirect:/admin/blogs";
    }
}
