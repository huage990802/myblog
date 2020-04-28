package com.xzh.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_tag")
public class Tag {
    @Id
    @GeneratedValue
    private Long id;//id

    @ManyToMany(mappedBy = "tags" )
    private List<Blog> blogs =new ArrayList<>();
    @NotBlank(message = "分类名称不能为空")
    private String name; //标签名字


    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag() {
    }

}
