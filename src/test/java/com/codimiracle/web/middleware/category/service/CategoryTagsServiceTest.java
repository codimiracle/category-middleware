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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryTagsServiceTest {
    @Resource
    private CategoryTagsService categoryTagsService;
    @Resource
    private TagService tagService;

    @Test
    void findByCategoryId() {
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setTagId("1023");
        categoryTag.setCategoryId("4052");
        categoryTagsService.save(categoryTag);
        CategoryTag result = categoryTagsService.findById(categoryTag.getId());
        assertNotNull(result);
        assertEquals(categoryTag.getCategoryId(), result.getCategoryId());
        assertEquals(categoryTag.getTagId(), result.getTagId());
        assertNotNull(result.getId());
    }

    @Test
    void findTagByCategoryId() {
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setTagId("4300");
        categoryTag.setCategoryId("2122");
        categoryTagsService.save(categoryTag);
        List<CategoryTag> result = categoryTagsService.findByCategoryId(categoryTag.getCategoryId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(categoryTag.getTagId(), result.get(0).getTagId());
        assertEquals(categoryTag.getCategoryId(), result.get(0).getCategoryId());
    }

    @Test
    void updateAttachingTags() {
        final String categoryId = "1";
        final List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setId("1");
        tag.setName("TagName");
        tagList.add(tag);
        Tag anotherTag = new Tag();
        anotherTag.setId("2");
        anotherTag.setName("Another Tag");
        tagList.add(anotherTag);
        // add one
        categoryTagsService.updateAttachingTags(categoryId, tagList);
        List<CategoryTag> addResult = categoryTagsService.findByCategoryId(categoryId);
        // delete all tags
        tagList.clear();
        categoryTagsService.updateAttachingTags(categoryId, tagList);
        List<CategoryTag> deleteResult = categoryTagsService.findByCategoryId(categoryId);
        assertTrue(deleteResult.isEmpty());

        // reuse old tag
        tagList.clear();
        tagList.add(tag);
        categoryTagsService.updateAttachingTags(categoryId, tagList);
        List<CategoryTag> reuseResult = categoryTagsService.findByCategoryId(categoryId);
        assertEquals(1, reuseResult.size());
        assertEquals(addResult.get(0).getId(), reuseResult.get(0).getId());
        // ensure findByCategoryId doesn't return deleted CategoryTag
        assertFalse(reuseResult.get(0).getDeleted());

        // mix new and old tags
        tagList.clear();
        Tag newTag = new Tag();
        newTag.setId("3");
        newTag.setName("Other Tag");
        tagList.add(anotherTag);
        tagList.add(newTag);
        categoryTagsService.updateAttachingTags(categoryId, tagList);
        List<CategoryTag> mixUpdateResult = categoryTagsService.findByCategoryId(categoryId);
        assertEquals(2, mixUpdateResult.size());
        assertEquals(mixUpdateResult.get(0).getId(), addResult.get(1).getId());
        assertFalse(mixUpdateResult.get(0).getDeleted());
        assertEquals(newTag.getId(), mixUpdateResult.get(1).getTagId());
    }

    @Test
    void findTagByCategoryIdIntegrally() {
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setTagId("6530");
        categoryTag.setCategoryId("1022");
        Tag tag = new Tag();
        tag.setId(categoryTag.getTagId());
        tag.setName("Test Tag");
        tagService.save(tag);
        categoryTagsService.save(categoryTag);
        List<TagVO> tags = categoryTagsService.findTagByCategoryIdIntegrally(categoryTag.getCategoryId());
        assertEquals(1, tags.size());
        TagVO tagVO = tags.get(0);
        assertEquals(tagVO.getId(), categoryTag.getTagId());
        assertEquals(tagVO.getName(), tag.getName());
    }

    @Test
    void findByCategoryIdWithDeleted() {
        final String categoryId = "86600";
        for (int i = 0; i < 100; i++) {
            CategoryTag categoryTag = new CategoryTag();
            categoryTag.setTagId(String.format("32%02d", i));
            categoryTag.setCategoryId(categoryId);
            categoryTagsService.save(categoryTag);
        }
        List<CategoryTag> categoryTagList = categoryTagsService.findByCategoryIdWithDeleted(categoryId);
        assertEquals(100, categoryTagList.size());
        CategoryTag firstCategoryTag = categoryTagList.get(0);
        assertEquals("3200", firstCategoryTag.getTagId());
        assertEquals(categoryId, firstCategoryTag.getCategoryId());
        CategoryTag lastCategoryTag = categoryTagList.get(categoryTagList.size() - 1);
        assertEquals("3299", lastCategoryTag.getTagId());
        assertEquals(categoryId, lastCategoryTag.getCategoryId());
    }
}