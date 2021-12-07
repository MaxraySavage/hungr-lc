package org.launchcode.hungr.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends AbstractEntity{

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @NotNull
    private String username;

    @NotNull
    private String pwHash;

    @OneToMany(orphanRemoval = true, fetch= FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private final List<Recipe> ownedRecipes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Recipe> favoriteRecipes = new ArrayList<>();

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.pwHash = encoder.encode(password);
    }

    public String getUsername() {
        return username;
    }

    public List<Recipe> getOwnedRecipes() {
        return ownedRecipes;
    }

    public List<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }


    public void addFavoriteRecipe(Recipe recipe){
        favoriteRecipes.add(recipe);
    }

    public boolean removeFavoriteRecipe(Recipe recipe){
        return favoriteRecipes.remove(recipe);
    }


}
