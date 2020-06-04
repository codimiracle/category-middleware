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
import com.codimiracle.web.middleware.category.pojo.po.Category;
import com.codimiracle.web.middleware.category.pojo.vo.CategoryVO;
import com.codimiracle.web.middleware.category.service.CategoryService;
import com.codimiracle.web.middleware.category.service.CategoryTagsService;
import com.codimiracle.web.middleware.category.service.TagService;
import com.codimiracle.web.mybatis.contract.support.vo.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional
public class CategoryServiceImpl extends AbstractService<String, Category, CategoryVO> implements CategoryService {
    @Resource
    private CategoryTagsService categoryTagsService;
    @Resource
    private TagService tagService;

    private void settingParentId(Category category) {
        if (Objects.nonNull(category.getParent())) {
            category.setParentId(category.getParent().getId());
        }
    }

    @Override
    protected CategoryVO mutate(CategoryVO inflatedObject) {
        if (Objects.nonNull(inflatedObject.getParentId())) {
            inflatedObject.setParent(findByIdIntegrally(inflatedObject.getParentId()));
        }
        inflatedObject.setTagList(categoryTagsService.findTagByCategoryIdIntegrally(inflatedObject.getId()));
        return super.mutate(inflatedObject);
    }

    @Override
    public void save(Category model) {
        settingParentId(model);
        model.setCreatedAt(new Date());
        model.setUpdatedAt(new Date());
        super.save(model);
        if (Objects.nonNull(model.getTagList())) {
            categoryTagsService.updateAttachingTags(model.getId(), model.getTagList());
        }
    }

    @Override
    public void update(Category model) {
        model.setUpdatedAt(new Date());
        super.update(model);
    }
}
