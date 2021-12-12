package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.data.UserRepository;
import org.launchcode.hungr.models.Recipe;
import org.launchcode.hungr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @ModelAttribute
    public void addUserAttributes(Model model, HttpServletRequest request) {
        Object userObj = request.getAttribute("user");
        if(userObj != null && userObj instanceof User){
            model.addAttribute("user", (User) userObj);
        }
    }

    @GetMapping("profile")
    public String displayProfile(Model model){
        model.addAttribute("profileOwner", model.getAttribute("user"));
        return "users/profile";
    }

    @GetMapping("profile/{username}")
    public String displayProfile(Model model, @PathVariable String username){
        User profileOwner = userRepository.findByUsername(username);

        if(profileOwner == null) {
            // the expected user was not found
            return "redirect:/recipes";
        }
        model.addAttribute("profileOwner", profileOwner);

        return "users/profile";
    }

    @PostMapping("favorite")
    @ResponseBody
    public String processFavoriteRecipeRequest(@RequestParam Integer recipeId, Model model, HttpServletResponse response){
        // TODO: This is currently not idempotent, should be refactored

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()){
            // target recipe not found
            response.setStatus(404);
            return "recipe not found";
        }
        Recipe targetRecipe = optionalRecipe.get();

        Object requestingUserObj = model.getAttribute("user");

        if(requestingUserObj == null || !(requestingUserObj instanceof User)){
            // no valid requesting user
            response.setStatus(401);
            return "request must be from authenticated user";
        }
        User requestingUser = (User) requestingUserObj;

        // If we are here we have a valid requesting user and recipe


        // if recipe is already favorited we want to unfavorite it

        if(requestingUser.removeFavoriteRecipe(targetRecipe)){
            // remove returns true if recipe found and removed
            response.setStatus(200);
            userRepository.save(requestingUser);
            return "unfavorited recipe successfully";
        }

        requestingUser.addFavoriteRecipe(targetRecipe);
        userRepository.save(requestingUser);
        response.setStatus(200);
        return "favorited recipe successfully";
    }

}
