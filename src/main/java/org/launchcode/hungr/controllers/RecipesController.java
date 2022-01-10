package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.IngredientRepository;
import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.data.RecipeStepRepository;
import org.launchcode.hungr.models.Ingredient;
import org.launchcode.hungr.models.Recipe;
import org.launchcode.hungr.models.RecipeStep;
import org.launchcode.hungr.models.User;
import org.launchcode.hungr.models.dto.CreateRecipeFormDTO;
import org.launchcode.hungr.models.dto.EditRecipeFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("recipes")
public class RecipesController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @ModelAttribute
    public void setUserInModel(Model model, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        model.addAttribute("user", user);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        Object userObject = request.getAttribute("user");
        if(userObject != null && userObject instanceof User){
            return (User) userObject;
        }
        return null;
    }

    @GetMapping
    public String displayRecipes(Model model){
        model.addAttribute("title", "All Recipes");
        model.addAttribute("subtitle", "A list of all public recipes currently in the database.");
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes/index";
    }

    @GetMapping("/details/{recipeId}")
    public String displayRecipe(HttpServletRequest request, Model model, @PathVariable int recipeId){
        Optional<Recipe> optionRecipe = recipeRepository.findById(recipeId);

        if(optionRecipe.isEmpty()){
            return "redirect:/recipes";
        }
        Recipe recipe = optionRecipe.get();
        User user = getUserFromRequest(request);
        boolean userIsAuthor = recipe.getAuthor().equals(user);
        boolean recipeIsFavorite = recipe.isUserFavorite(user);

        model.addAttribute("recipeIsFavorite", recipeIsFavorite);
        model.addAttribute("userIsAuthor", userIsAuthor);
        model.addAttribute("title", "Recipe Details");
        model.addAttribute("recipe", recipe);
        return "recipes/details";
    }

    @GetMapping("create")
    public String renderCreateRecipeForm(Model model){
        model.addAttribute("title", "Add a Recipe");
        model.addAttribute("createRecipeFormDTO", new CreateRecipeFormDTO());
        return "recipes/create";
    }

    @PostMapping("create")
    public String processCreateRecipeForm( Model model, @ModelAttribute @Valid CreateRecipeFormDTO createRecipeFormDTO, Errors errors, HttpServletRequest request){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add a Recipe");
            return "recipes/create";
        }
        Recipe newRecipe = new Recipe(createRecipeFormDTO.getName(), createRecipeFormDTO.getShortDescription());
        newRecipe.setAuthor(getUserFromRequest(request));
        newRecipe.setIcon(createRecipeFormDTO.getIcon());
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        for( String ingredientName : createRecipeFormDTO.getIngredients()) {
            Ingredient newIngredient = new Ingredient(ingredientName);
            newIngredient.setRecipe(savedRecipe);
            ingredientRepository.save(newIngredient);
        }
        for( String stepText : createRecipeFormDTO.getSteps()) {
            RecipeStep newStep = new RecipeStep(stepText);
            newStep.setRecipe(savedRecipe);
            recipeStepRepository.save(newStep);
        }
        return "redirect:/recipes/details/" + savedRecipe.getId();
    }

    @GetMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId, HttpServletRequest request){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            // recipe not found in database
            return "redirect:/recipes";
        }
        Recipe recipe = optionalRecipe.get();
        boolean userIsAuthor = recipe.getAuthor().equals(getUserFromRequest(request));
        if(! userIsAuthor) {
            return "redirect:/recipes";
        }

        EditRecipeFormDTO editRecipeFormDTO = new EditRecipeFormDTO(recipe);
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("editRecipeFormDTO", editRecipeFormDTO);

        return "recipes/edit";
    }

    @PostMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId, @ModelAttribute @Valid EditRecipeFormDTO editRecipeFormDTO, Errors errors, HttpServletRequest request){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            // recipeId is not found in the database, therefore can't be edited
            return "redirect:/recipes";
        }

        Recipe originalRecipe = optionalRecipe.get();
        boolean userIsAuthor = originalRecipe.getAuthor().equals(getUserFromRequest(request));

        if(!userIsAuthor) {
            // the user trying to edit the recipe is not the author, they shouldn't be allowed to edit it
            return "redirect:/recipes";
        }

        if(editRecipeFormDTO.getId() != recipeId) {
            // somehow the DTO is for a different recipe than this endpoint
            return "redirect:/recipes";
        }

        if(errors.hasErrors() ){
            // The proposed updated recipe doesn't meet validation requirements and shouldn't be saved to the database
            model.addAttribute("title", "Edit Recipe");
            model.addAttribute("editRecipeFormDTO", editRecipeFormDTO);
            return "recipes/edit";
        }

        // edit recipe request meets requirements and is acted on
        editRecipeFormDTO.mapFieldsOntoRecipe(originalRecipe);
        originalRecipe.getIngredients().clear();
        originalRecipe.getSteps().clear();
        Recipe savedRecipe = recipeRepository.save(originalRecipe);
        for( String ingredientName : editRecipeFormDTO.getIngredients()) {
            Ingredient newIngredient = new Ingredient(ingredientName);
            newIngredient.setRecipe(savedRecipe);
            ingredientRepository.save(newIngredient);
        }
        for( String stepText : editRecipeFormDTO.getSteps()) {
            RecipeStep newStep = new RecipeStep(stepText);
            newStep.setRecipe(originalRecipe);
            recipeStepRepository.save(newStep);
        }
        return "redirect:/recipes/details/" + originalRecipe.getId();
    }

    @PostMapping("delete")
    public String processDeleteRecipe(@RequestParam Integer recipeId, Model model, HttpServletRequest request){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()){
            // delete request for an id that is not in the database
            return "redirect:/recipes";
        }
        Recipe recipeToDelete = optionalRecipe.get();

        boolean userIsAuthor = recipeToDelete.getAuthor().equals(getUserFromRequest(request));
        if(! userIsAuthor){
            // requesting user doesn't own the recipe and shouldn't be allowed to delete it
            return "redirect:/recipes";
        }

        recipeRepository.delete(recipeToDelete);
        return "redirect:/recipes";
    }

}
