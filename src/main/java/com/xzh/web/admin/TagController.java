package com.xzh.web.admin;

import com.xzh.po.Tag;
import com.xzh.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    TagServiceImpl tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)  Pageable pageable, Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String toInput(Model model)
    {
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @PostMapping("/tags/input")
    public String input(@Valid Tag tag, BindingResult result ,RedirectAttributes attributes){
        Tag tagByName = tagService.getTagByName(tag.getName());
        if(result.hasErrors()){
            return "admin/tags-input";
        }

        if(tag==null){
            attributes.addFlashAttribute("message","新增失败");
        }
        else {
            if(tagByName!=null){
                attributes.addFlashAttribute("message","标签名已存在，请勿重复添加");
            }
            else {
                tagService.saveTag(tag);
                attributes.addFlashAttribute("message", "新增成功");
            }
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable("id")Long id,Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }


    @PostMapping("/tags/input/{id}")
    public String editInput(@Valid  Tag tag, BindingResult result,@PathVariable("id") Long id,RedirectAttributes attributes){
        Tag tagByName = tagService.getTagByName(tag.getName());
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        if(tag==null){
            attributes.addFlashAttribute("message","修改失败");
        }
        else {
            if(tagByName!=null){
                attributes.addFlashAttribute("message","标签名已存在，请勿重复添加");
            }
            else {
                tagService.updateTag(id,tag);
                attributes.addFlashAttribute("message", "修改成功");
            }
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable("id") Long id,RedirectAttributes attributes){
        String name = tagService.getTag(id).getName();
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除标签名"+name+"成功！");
        return "redirect:/admin/tags";
    }
}
