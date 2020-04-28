package com.xzh.service;

import com.xzh.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Page<Tag> listTag(Pageable pageable);
    Tag getTag(Long id);
    Tag getTagByName(String name);
    void deleteTag(Long id);
    Tag updateTag(Long id, Tag tag);

    Tag saveTag(Tag tag);

    List<Tag> listTag();

    List<Tag> listTag(String ids);
    List<Tag> listTagTop(Integer size);
}
