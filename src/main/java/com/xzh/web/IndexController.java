package com.xzh.web;

import com.xzh.NotFoundException;
import com.xzh.po.Blog;
import com.xzh.po.Type;
import com.xzh.service.BlogService;
import com.xzh.service.TagService;
import com.xzh.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @Autowired
    TagService tagService;

    @Autowired
    TypeService typeService;

    @RequestMapping({"index","/","index.html"})
    public String toIndex(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageablem, Model model){
model.addAttribute("page",blogService.listBlog(pageablem));
model.addAttribute("types",typeService.listTypeTop(6));
model.addAttribute("tags",tagService.listTagTop(10));
model.addAttribute("recommendBlogs",blogService.listBlogTop(8));
        return "index";
    }

    @PostMapping("/serach")
    public String serach(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageablem, Model model,@RequestParam String query){
        model.addAttribute("page",blogService.listBlog(pageablem,"%"+query+"%"));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/footer/newblog")
    public String footBolg(Model model){
        List<Blog> blogs = blogService.listBlogTop(3);
        model.addAttribute("newBlogs",blogs);
        return "common/common::newBlogList";
    }

    @GetMapping("/adminfooter/newblog")
    public String adminfootBolg(Model model){
        List<Blog> blogs = blogService.listBlogTop(3);
        model.addAttribute("newBlogs",blogs);
        return "common/addmin_common::newBlogList";
    }
}
