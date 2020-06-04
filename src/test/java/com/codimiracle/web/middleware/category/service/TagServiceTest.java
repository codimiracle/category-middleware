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
import com.codimiracle.web.middleware.category.pojo.po.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TagServiceTest {
    @Resource
    private TagService tagService;

    @Test
    void findByNames() {
        for (int i = 0; i < 20; i++) {
            Tag tag = new Tag();
            tag.setName(String.format("Tag Tag Tag 1%02d", i));
            tagService.save(tag);
        }
        List<String> names = new ArrayList<>();
        names.add("Tag Tag Tag 100");
        names.add("Tag Tag Tag 105");
        names.add("Tag Tag Tag 110");
        names.add("Tag Tag Tag 115");
        names.add("Tag Tag Tag 119");
        List<Tag> tags = tagService.findByNames(names);
        for (int i = 0; i < tags.size(); i++) {
            assertEquals(names.get(i), tags.get(i).getName());
        }
    }

    @Test
    void findByIds() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Tag tag = new Tag();
            tag.setName(String.format("Tag By Id 1%02d", i));
            tagService.save(tag);
            ids.add(tag.getId());
        }
        List<Tag> tags = tagService.findByIds(ids);
        for (int i = 0; i < tags.size(); i++) {
            assertEquals(ids.get(i), tags.get(i).getId());
        }
    }

    @Test
    void save() {
        Tag tag1 = new Tag();
        tag1.setName("Hello Tag1");
        tagService.save(tag1);
        Tag tag = tagService.findById(tag1.getId());
        assertNotNull(tag.getCreatedAt());
        assertEquals(tag1.getName(), tag.getName());
    }

    @Test
    void update() {
        Tag tag = new Tag();
        tag.setName("Hello tag2");
        tagService.save(tag);
        Tag result = tagService.findById(tag.getId());
        Tag updatingTag = new Tag();
        updatingTag.setId(result.getId());
        updatingTag.setName("Changed Tag Name");
        tagService.update(updatingTag);
        Tag updatedTag = tagService.findById(updatingTag.getId());
        assertEquals(updatedTag.getName(), updatingTag.getName());
        assertNotNull(updatedTag.getUpdatedAt());
        assertEquals(updatedTag.getUpdatedAt(), updatingTag.getUpdatedAt());
    }
}