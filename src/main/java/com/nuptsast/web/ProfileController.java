package com.nuptsast.web;

import com.nuptsast.model.User;
import com.nuptsast.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    private Logger logger = Logger.getLogger(getClass());

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String profile(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext()
            .getAuthentication();
        String username = auth.getName();
        User user = userService.getUser(username);
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        if (user.getAuthority().equals("ROLE_ADMIN")) {
            return "redirect:/manage";
        }
        if (session.getAttribute("status") != null) {
            model.addAttribute("status", Boolean.FALSE);
        }
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateProfile(User user, Model model, HttpSession session) {
        if (StringUtils.isEmpty(user.getTargetDepartment())) {
            model.addAttribute("status", Boolean.FALSE);
            user.setUsername(
                ((User) session.getAttribute("user")).getUsername());
            return "profile";
        }
        logger.info("update " + user);
        User originUser = (User) session.getAttribute("user");
        originUser.setTargetDepartment(user.getTargetDepartment());
        userService.updateUser(originUser);
        model.addAttribute("status", Boolean.TRUE);
        user.setUsername(((User) session.getAttribute("user")).getUsername());
        return "redirect:/exam";
    }

}
