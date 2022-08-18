package com.xe.mynowcoder.controller;


import com.xe.mynowcoder.annotation.LoginRequired;
import com.xe.mynowcoder.entity.User;
import com.xe.mynowcoder.service.FollowService;
import com.xe.mynowcoder.service.LikeService;
import com.xe.mynowcoder.service.UserService;
import com.xe.mynowcoder.util.HostHolder;
import com.xe.mynowcoder.util.NowCoderConstant;
import com.xe.mynowcoder.util.NowCoderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController implements NowCoderConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${nowcoder.path.upload}")
    private String uploadPath;

    @Value("${nowcoder.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;


    //访问账号设置页面
    @LoginRequired
    @GetMapping(path = "/setting")
    public String getSettingPage(Model model) {
        return "/site/setting";
    }

//    // 更新头像路径
//    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
//    @ResponseBody
//    public String updateHeaderUrl(String fileName) {
//        if (StringUtils.isBlank(fileName)) {
//            return NowCoderUtil.getJSONString(1, "文件名不能为空!");
//        }
//
//        String url = headerBucketUrl + "/" + fileName;
//        userService.updateHeader(hostHolder.getUser().getId(), url);
//
//        return CommunityUtil.getJSONString(0);
//    }

    @LoginRequired
    @PostMapping(path = "/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        //修改文件名是防止多个用户上传的重名覆盖
        //原始名
        String fileName = headerImage.getOriginalFilename();
        //后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        //是否为空
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = NowCoderUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(应该提供web访问路径)
        // http://localhost:8083/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getUserId(), headerUrl);
        return "redirect:/index";
    }

    //TODO 因为每次访问都会先拦截去查询用户信息，持有用户信息，所以上传头像之后，返回首页头像就已经更新。

    @GetMapping(path = "/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                //得到输出流
                OutputStream os = response.getOutputStream();
                //读取文件得到输入流  小括号里会自动关闭，java7语法
                FileInputStream fis = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }


    // 个人主页(也是任意主页根据userId确定）
    @GetMapping(path = "/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getUserId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

}
