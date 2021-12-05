package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.UserRepository;
import org.launchcode.hungr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @ModelAttribute
    public void addUserAttributes(Model model, HttpServletRequest request) {
        Object userObj = request.getAttribute("user");
        if(userObj != null && userObj instanceof User){
            model.addAttribute("user", (User) userObj);
        }
    }

    @GetMapping("/profile")
    public String displayProfile(Model model){
        model.addAttribute("profileOwner", model.getAttribute("user"));
        return "users/profile";
    }

    @GetMapping("/profile/{username}")
    public String displayProfile(Model model, @PathVariable String username){
        User profileOwner = userRepository.findByUsername(username);

        if(profileOwner == null) {
            // the expected user was not found
            return "redirect:/recipes";
        }
        model.addAttribute("profileOwner", profileOwner);

        return "users/profile";
    }


}
