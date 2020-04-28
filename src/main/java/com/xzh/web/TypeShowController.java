package com.xzh.web;

import com.xzh.po.Blog;
import com.xzh.po.Type;
import com.xzh.service.BlogService;
import com.xzh.service.TypeService;
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
public class TypeShowController {

    @Autowired
    BlogService blogService;

    @Autowired
    TypeService typeService;
    @GetMapping("/types/{typeId}")
    public String type(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageablem, Model model,@PathVariable Long typeId){
        List<Type> types = typeService.listTypeTop(1000);
        if(typeId==-1){
            typeId=types.get(0).getId();
        }
        BlogQuery blogQuery =new BlogQuery();
        blogQuery.setTypeId(typeId);
        model.addAttribute("types",types);
        Page<Blog> blogs = blogService.listBlogByTypeId(pageablem,blogQuery);
        model.addAttribute("page",blogs);
        model.addAttribute("activeTypeId",typeId);
        return "types";
    }

}
