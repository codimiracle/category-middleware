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
import com.codimiracle.web.middleware.category.pojo.po.Tag;
import com.codimiracle.web.middleware.category.pojo.vo.TagVO;
import com.codimiracle.web.middleware.category.service.TagService;
import com.codimiracle.web.mybatis.contract.support.vo.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl extends AbstractService<String, Tag, TagVO> implements TagService {
    @Override
    public void save(Tag model) {
        model.setCreatedAt(new Date());
        super.save(model);
    }

    @Override
    public void update(Tag model) {
        model.setUpdatedAt(new Date());
        super.update(model);
    }

    @Override
    public List<Tag> findByNames(List<String> names) {
        Condition condition = new Condition(Tag.class);
        condition.createCriteria()
                .andIn("name", names);
        return findByCondition(condition);
    }

    @Override
    public List<Tag> findByIds(List<String> ids) {
        Condition condition = new Condition(Tag.class);
        condition.createCriteria()
                .andIn("id", ids);
        return findByCondition(condition);
    }
}
