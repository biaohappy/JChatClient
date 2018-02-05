package com.xiaobiao.chatclient.controller;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.xiaobiao.chatclient.model.Friend;
import com.xiaobiao.chatclient.model.Group;
import com.xiaobiao.chatclient.model.Mine;
import com.xiaobiao.chatclient.param.ChatFriend;
import com.xiaobiao.chatclient.param.ChatGroup;
import com.xiaobiao.chatclient.param.ChatMine;
import com.xiaobiao.chatclient.result.FriendResult;
import com.xiaobiao.chatclient.result.ImResult;
import com.xiaobiao.chatclient.service.ChatFriendService;
import com.xiaobiao.chatclient.service.ChatGroupService;
import com.xiaobiao.chatclient.service.ChatMineService;
import com.xiaobiao.shirorealm.ActiveUser;
import com.xiaobiao.utils.exception.ChatErrorEnum;
import com.xiaobiao.utils.exception.ChatException;
import com.xiaobiao.utils.util.BeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2017-12-07 11:17
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Controller
@Slf4j
@RequestMapping("/")
public class LoginController {

    @Autowired
    private ChatMineService chatMineService;

    @Autowired
    private ChatFriendService chatFriendService;

    @Autowired
    private ChatGroupService chatGroupService;


    /**
     * 散列次数
     */
    //若配置文件中无count属性，则给一个默认值1
    @Value("${count:1}")
    private String count;

    /**
     * 盐
     */
    @Value("${salt}")
    private String salt;


    /**
     * 登录界面
     *
     * @return
     */
    @RequestMapping("login")
    public String login() {
        return "manager/login";
    }

    /**
     * 执行登录
     *
     * @param request
     * @return
     */
    @RequestMapping("signIn")
    public String signIn(HttpServletRequest request) {
        UsernamePasswordToken token = null;
        try {
            String userCode = request.getParameter("userCode");
            String password = request.getParameter("password");
            request.setAttribute("userCode", userCode);
            token = new UsernamePasswordToken(userCode, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            log.info("userCode:" + userCode + ",password:" + password);
            return "redirect:toMain";
        } catch (UnknownAccountException e) {
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341002.getErrorDesc());
            token.clear();
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (IncorrectCredentialsException e) {
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341003.getErrorDesc());
            token.clear();
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (AuthenticationException e) {
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341004.getErrorDesc());
            token.clear();
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (ChatException e) {
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341005.getErrorDesc());
            token.clear();
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (Exception e) {
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341FFF.getErrorDesc());
            token.clear();
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        }
        return "manager/login";
    }

    /**
     * 系统首页
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "toMain")
    public String first(Model model, HttpServletRequest request) {
        try {
            //主体
            Subject subject = SecurityUtils.getSubject();
            //身份
            ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
            //查询IM信息
            ChatMine chatMine = chatMineService.queryChatMine(activeUser.getUserCode());
            model.addAttribute("chatMine", chatMine);
            model.addAttribute("activeUser", activeUser);
            HttpSession session = request.getSession();
            session.setAttribute("user", activeUser.getUserCode());
        } catch (ChatException e) {
            request.setAttribute("errorCode", e.getCode());
            request.setAttribute("errorMsg", e.getMessage());
            log.error("failed to signIn, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (Exception e) {
            request.setAttribute("errorCode", ChatErrorEnum.ERROR_CODE_341FFF.getErrorCode());
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341FFF.getErrorDesc());
            log.error("failed to toMain, CAUSE:{}", Throwables.getStackTraceAsString(e));
        }
        return "manager/index";
    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping("logout")
    public String logOut() {
        return "manager/login";
    }


    /**
     * 初始化主页面和获取好友列表
     * @param request
     * @return
     */
    @RequestMapping("findFriends")
    @ResponseBody
    public String friends(HttpServletRequest request){
        Gson json = new Gson();
        String jsonStr = null;
        try {
            String userCode = request.getParameter("userCode");
            ChatMine mineParam = new ChatMine();
            mineParam.setUsercode(userCode);
            mineParam.setStatus("online");
            chatMineService.updataChatMine(mineParam);
            ChatMine chatMine = chatMineService.queryChatMine(userCode);
            Mine mine = new Mine();
            BeanMapper.copy(chatMine, mine);
            List<ChatGroup> groupList = chatGroupService.queryGroups(chatMine.getId());
            List<ChatFriend> chatFriendList = chatFriendService.queryFriends(chatMine.getId());
            List<Friend> friendList = new ArrayList();
            for (ChatGroup group : groupList) {
                Friend friend = new Friend();
                friend.setGroupname(group.getGroupname());
                friend.setId(group.getId());
                List<Mine> list = new ArrayList<Mine>();
                for (ChatFriend chatFriend : chatFriendList) {
                    if (chatFriend.getGroupId().equals(group.getId())) {
                        Mine friendMine = new Mine();
                        friendMine.setId(chatFriend.getFriendId());
                        friendMine.setUsername(chatFriend.getUserName());
                        friendMine.setAvatar(chatFriend.getAvatar());
                        friendMine.setSign(chatFriend.getSign());
                        friendMine.setStatus(chatFriend.getStatus());
                        list.add(friendMine);
                    }
                }
                friend.setList(list);
                friendList.add(friend);
            }
            FriendResult friendResult = new FriendResult();
            friendResult.setMine(mine);
            friendResult.setFriend(friendList);
            friendResult.setGroup(new ArrayList<Group>());

            ImResult imResult = new ImResult();
            imResult.setCode("0");
            imResult.setMsg("查询成功");
            imResult.setData(friendResult);
            jsonStr = json.toJson(imResult);
        } catch (ChatException e) {
            request.setAttribute("errorCode", e.getCode());
            request.setAttribute("errorMsg", e.getMessage());
            log.error("failed to findFriends, CAUSE:{}", Throwables.getStackTraceAsString(e));
        } catch (Exception e) {
            request.setAttribute("errorCode", ChatErrorEnum.ERROR_CODE_341FFF.getErrorCode());
            request.setAttribute("errorMsg", ChatErrorEnum.ERROR_CODE_341FFF.getErrorDesc());
            log.error("failed to findFriends, CAUSE:{}", Throwables.getStackTraceAsString(e));
        }
        return jsonStr;
    }
}
