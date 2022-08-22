package com.xe.mynowcoder.controller;


import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.Page;
import com.xe.mynowcoder.service.ElasticsearchService;
import com.xe.mynowcoder.service.LikeService;
import com.xe.mynowcoder.service.UserService;
import com.xe.mynowcoder.util.NowCoderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements NowCoderConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=xxx  问号拼接
    @GetMapping(path = "/search")
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        Map<String,Object> searchResult =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null) {
            List<DiscussPost> list = (List<DiscussPost>) searchResult.get("list");
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getDiscussPostId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        // 分页信息
        page.setPath("/search?keyword=" + keyword);
        if(searchResult == null){
            page.setRows(0);

        }else {
            Long total = (Long) searchResult.get("total");
            int rows = total.intValue();
            page.setRows(rows);

        }
        return "/site/search";
    }

}
