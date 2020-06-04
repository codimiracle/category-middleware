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
import com.codimiracle.web.middleware.category.pojo.po.Category;
import com.codimiracle.web.middleware.category.pojo.po.Tag;
import com.codimiracle.web.middleware.category.pojo.vo.CategoryVO;
import com.codimiracle.web.middleware.category.pojo.vo.TagVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;

    @Test
    void save() {
        Category category = new Category();
        category.setName("Hello Category");
        categoryService.save(category);
        Category result = categoryService.findById(category.getId());
        assertEquals(category.getName(), result.getName());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void update() {
        Category category = new Category();
        category.setName("Hi Category");
        categoryService.save(category);
        Category result = categoryService.findById(category.getId());
        Category updatingCategory = new Category();
        updatingCategory.setId(result.getId());
        updatingCategory.setName("Category Category");
        categoryService.update(updatingCategory);

        Category updatedCategory = categoryService.findById(result.getId());
        assertEquals(updatingCategory.getName(), updatedCategory.getName());
        assertNotNull(updatedCategory.getUpdatedAt());
        assertTrue(updatedCategory.getUpdatedAt().getTime() > result.getUpdatedAt().getTime());
    }

    @Test
    void findByCategoryIdIntegrally() {
        Category category = new Category();
        category.setName("Hello world");
        List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("Hello");
        tagService.save(tag);
        tagList.add(tag);
        category.setTagList(tagList);
        categoryService.save(category);
        CategoryVO categoryVO = categoryService.findByIdIntegrally(category.getId());
        assertNotNull(categoryVO);
        assertEquals(category.getName(), categoryVO.getName());
        List<TagVO> tags = categoryVO.getTagList();
        assertEquals(1, tags.size());
        assertEquals(tags.get(0).getId(), tag.getId());
    }

    @Test
    public void saveCategoryWithParent() {
        // create parent category
        Category rootCategory = new Category();
        rootCategory.setName("父类别");
        categoryService.save(rootCategory);
        // create sub category
        Category subCategory = new Category();
        subCategory.setName("子类别");
        subCategory.setParent(rootCategory);
        categoryService.save(subCategory);
        // retrieve category
        CategoryVO categoryVO = categoryService.findByIdIntegrally(subCategory.getId());
        assertNotNull(categoryVO);
        assertEquals(subCategory.getName(), categoryVO.getName());
        assertNotNull(categoryVO.getParent());
        assertNotNull(rootCategory.getId(), categoryVO.getParent().getId());
        assertEquals(rootCategory.getName(), categoryVO.getParent().getName());
    }
}