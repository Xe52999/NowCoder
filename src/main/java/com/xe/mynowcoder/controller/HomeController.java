package com.xe.mynowcoder.controller;

import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.Page;
import com.xe.mynowcoder.service.DiscussPostService;
import com.xe.mynowcoder.service.LikeService;
import com.xe.mynowcoder.service.UserService;
import com.xe.mynowcoder.util.NowCoderConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 首页的控制层
 * @author xjx
 * @DATE 2022/8/15
 */
@Controller
public class HomeController implements NowCoderConstant {
    private  static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        //不需要将page重新加入model
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.

        //前端传入了current 属性放在了page中.不传默认为1
        logger.info("调用了首页分页方法，当前页为"+page.getCurrent());

        page.setRows(discussPostService.findDiscussPostsRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(), 0);

        List<Map<String,Object>> discussPosts = new ArrayList<>();

        if (list != null) {
            for (DiscussPost post : list) {
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                map.put("user",userService.findUserById(post.getUserId()));

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getDiscussPostId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }


        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "/error/500";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }


}
