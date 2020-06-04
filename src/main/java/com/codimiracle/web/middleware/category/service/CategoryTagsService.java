package com.codimiracle.web.middleware.category.service;
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
import com.codimiracle.web.mybatis.contract.Service;

import java.util.List;

public interface CategoryTagsService extends Service<String, CategoryTag> {
    List<CategoryTag> findByCategoryId(String categoryId);

    void updateAttachingTags(String categoryId, List<Tag> tags);

    List<Tag> findTagByCategoryId(String categoryId);

    List<TagVO> findTagByCategoryIdIntegrally(String categoryId);

    List<CategoryTag> findByCategoryIdWithDeleted(String categoryId);
}
