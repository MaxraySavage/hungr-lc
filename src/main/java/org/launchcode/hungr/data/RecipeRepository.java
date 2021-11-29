package org.launchcode.hungr.data;

import org.launchcode.hungr.models.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
}
