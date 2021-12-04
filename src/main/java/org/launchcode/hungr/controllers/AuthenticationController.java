package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.UserRepository;
import org.launchcode.hungr.models.User;
import org.launchcode.hungr.models.dto.LoginFormDTO;
import org.launchcode.hungr.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("/signup")
    public String displaySignupForm(Model model){
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "hungr signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignupForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "signup";
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "signup";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "signup";
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());

        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();

        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }

        setUserInSession(request.getSession(), theUser);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
