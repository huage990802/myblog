package com.xzh.web.admin;

import com.xzh.po.Type;
import com.xzh.service.impl.TypeServiceImpl;
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
public class TypeController {

    @Autowired
    TypeServiceImpl typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)  Pageable pageable, Model model){
        model.addAttribute("page",    typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String toInput(Model model)
    {
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @PostMapping("/types/input")
    public String input(@Valid Type type, BindingResult result ,RedirectAttributes attributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        System.out.println(typeByName);
        if(result.hasErrors()){
            return "admin/types-input";
        }

        if(type==null){
            attributes.addFlashAttribute("message","新增失败");
        }
        else {
            if(typeByName!=null){
                attributes.addFlashAttribute("message","分类名已存在，请勿重复添加");
            }
           else {
                typeService.saveType(type);
                attributes.addFlashAttribute("message", "新增成功");
            }
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id")Long id,Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }


    @PostMapping("/types/input/{id}")
    public String editInput(@Valid Type type, BindingResult result,@PathVariable("id") Long id,RedirectAttributes attributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        System.out.println(typeByName);
        if(result.hasErrors()){
            return "admin/types-input";
        }
        if(type==null){
            attributes.addFlashAttribute("message","修改失败");
        }
        else {
            if(typeByName!=null){
                attributes.addFlashAttribute("message","分类名已存在，请勿重复添加");
            }
            else {
                typeService.updateType(id,type);
                attributes.addFlashAttribute("message", "修改成功");
            }
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable("id") Long id,RedirectAttributes attributes){
        String name = typeService.getType(id).getName();
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除分类名"+name+"成功！");
        return "redirect:/admin/types";
    }
}
