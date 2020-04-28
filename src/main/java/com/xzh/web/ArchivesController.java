package com.xzh.web;

import com.xzh.po.Blog;
import com.xzh.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
public class ArchivesController {

    @Autowired
    BlogService blogService;
    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap", blogService.archiveBlog());
        Map<String, List<Blog>> map = blogService.archiveBlog();
        System.out.println(map);
        model.addAttribute("blogCount", blogService.countBlog());
        return "archives";
    }
}
