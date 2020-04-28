package com.xzh.web;

import com.xzh.po.Blog;
import com.xzh.po.Tag;
import com.xzh.service.BlogService;
import com.xzh.service.TagService;
import com.xzh.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class TagsShowController {
    @Autowired
    BlogService blogService;

    @Autowired
   TagService tagService;
    @GetMapping("/tags/{tagId}")
    public String type(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageablem, Model model, @PathVariable Long tagId){
        List<Tag> tags = tagService.listTagTop(1000);
        if(tagId==-1){
            tagId=tags.get(0).getId();
        }
        BlogQuery blogQuery =new BlogQuery();
        model.addAttribute("tags",tags);
        Page<Blog> blogs = blogService.listBlogByTagId(pageablem,tagId);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTagId",tagId);
        return "tags";

    }
}
