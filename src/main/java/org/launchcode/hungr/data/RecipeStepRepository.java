package org.launchcode.hungr.data;

import org.launchcode.hungr.models.RecipeStep;
import org.springframework.data.repository.CrudRepository;

public interface RecipeStepRepository extends CrudRepository<RecipeStep, Integer> {
}
