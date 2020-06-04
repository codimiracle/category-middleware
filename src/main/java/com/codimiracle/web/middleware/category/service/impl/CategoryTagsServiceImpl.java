package com.codimiracle.web.middleware.category.service.impl;
/*
 * MIT License
 *
 * Copyright (c) 2020 codimiracle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, Publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import com.codimiracle.web.middleware.category.pojo.po.CategoryTag;
import com.codimiracle.web.middleware.category.pojo.po.Tag;
import com.codimiracle.web.middleware.category.pojo.vo.TagVO;
import com.codimiracle.web.middleware.category.service.CategoryTagsService;
import com.codimiracle.web.middleware.category.service.TagService;
import com.codimiracle.web.mybatis.contract.AbstractService;
import com.codimiracle.web.mybatis.contract.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryTagsServiceImpl extends AbstractService<String, CategoryTag> implements CategoryTagsService {
    @Resource
    private TagService tagService;

    @Override
    public void save(List<CategoryTag> models) {
        models.forEach(categoryTag -> {
            categoryTag.setDeleted(false);
        });
        super.save(models);
    }

    private List<CategoryTag> findByCategoryId(String categoryId, boolean withDeleted) {
        Condition condition = new Condition(CategoryTag.class);
        condition.createCriteria()
                .andEqualTo("categoryId", categoryId);
        if (!withDeleted) {
            condition.and().andEqualTo("deleted", false);
        }
        return findByCondition(condition);
    }

    @Override
    public List<CategoryTag> findByCategoryId(String categoryId) {
        return findByCategoryId(categoryId, false);
    }

    @Override
    public List<CategoryTag> findByCategoryIdWithDeleted(String categoryId) {
        return findByCategoryId(categoryId, true);
    }

    @Override
    public void updateAttachingTags(String categoryId, List<Tag> tags) {
        List<CategoryTag> attachedTags = findByCategoryIdWithDeleted(categoryId);
        Map<String, CategoryTag> needToAttachTags = tags.stream().collect(Collectors.toMap(Tag::getId, tag -> {
            CategoryTag categoryTag = new CategoryTag();
            categoryTag.setCategoryId(categoryId);
            categoryTag.setTagId(tag.getId());
            return categoryTag;
        }));
        if (needToAttachTags.containsKey(null)) {
            throw new ServiceException("Can not handle unsaved tag!");
        }
        attachedTags.forEach((categoryTag) -> {
            if (needToAttachTags.containsKey(categoryTag.getTagId())) {
                categoryTag.setDeleted(false);
            } else {
                categoryTag.setDeleted(true);
            }
            needToAttachTags.remove(categoryTag.getTagId());
        });

        // needToAttachTags is need to append now
        if (!needToAttachTags.isEmpty()) {
            save(new ArrayList<>(needToAttachTags.values()));
        }
        // update exists tags state.
        attachedTags.forEach(this::update);

    }

    @Override
    public List<Tag> findTagByCategoryId(String categoryId) {
        return findByCategoryId(categoryId).stream().map((categoryTag -> tagService.findById(categoryTag.getTagId()))).collect(Collectors.toList());
    }

    @Override
    public List<TagVO> findTagByCategoryIdIntegrally(String categoryId) {
        return findByCategoryId(categoryId).stream().map((categoryTag -> tagService.findByIdIntegrally(categoryTag.getTagId()))).collect(Collectors.toList());
    }
}
