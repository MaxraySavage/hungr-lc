# Hungr - a recipe sharing platform
Hungr is a web application for saving and sharing recipes between users.  It was built with Spring MVC, thymeleaf, Bulma, hibernate, and MySQL.

Hungr is deployed on Heroku [https://hungr-dev.herokuapp.com/](https://hungr-dev.herokuapp.com/) however the deployed version may not mirror the current version here in the repo.

## Goals
Create a way for people to easily save, share and try out new recipes without having to read a novel and fight through a bunch of popup advertisements.

## Technologies Used
Hungr is built with the following technologies:
* Java
* Spring Boot
* Hibernate
* MySQL
* Bulma
* HTML
* Thymeleaf
* CSS
* Javascript

### Why these technologies?
I chose to use the Spring Boot, ThymeLeaf, MySQL set of technologies because I thought of this project as a capstone of sorts with the LaunchCode bootcamp curriculum.

However, instead of using bootstrap, I chose to use Bulma because I liked how it looks and because Bulma includes no Javascript.
I learned a lot by writing the javascript to make all my forms and other components work as I wanted them to.

Bulma did have serious downsides and I don't think I would use it again. 
#### Bulma does not meet important accessibility criteria
Bulma doesn't meet contrast guidelines and a lot of the suggested ways of using Bulma's components in the docs are decidedly *un*-semantic.
I regret not realizing the extent of these issues until late in development.

## How it was built
1. I initially mocked up how I wanted the app to look using Bulma and basic HTML. This helped me get a sense of how the project might look and what functionality I would need.
2. I decided on a small set of initial features so that I could get a working project up and running quickly. I also defined a recipe as only a title and description at first.
   1. Create a recipe
   2. Look at a recipe
   3. Edit a recipe
   4. Delete a recipe
3. I expanded recipe objects to include an Ingredient list.
4. Recipes were again expanded to include a list of steps.
5. I added User objects, authentication and authorization.
6. I restricted Delete and Edit Recipe actions to only users who owned the recipe in question
7. Search
8. Favorite Recipes
9. User Profiles


## Features
* User Registration
  * Users can create a new account with a username and password
* User Login
  * Users can login with a valid username and password.
  * Valid login results in a session being created on the server and cookie being stored on the client.
* User Logout
  * Users can logout, invalidating their session and removing their session cookie from local storage.
* Recipe Creation
  * Users can create new recipes consisting of a recipe name, description, ingredients and steps.
* Recipe Edit
  * Users can edit a recipe that they created. 
  * Users may not edit recipes created by other users.
* Recipe Delete
  * Users can delete a recipe that they created.
  * Users may not delete recipes created by other users.
* Recipe View
  * Users can view a recipe details page that shows all recipe information including how many users have added that recipe to their favorites.
* Search Recipes
  * Users can search the recipe database to find recipes.
* Favorite Recipes
  * Users can add a recipe to their favorites.
* User Profile
  * Users can view a list of all the recipes they have created as well as all the recipes they have favorited.
  
## Next Steps
* Recipe image upload
* Accessibility Audit
* Add a random recipe display on the landing page
* Follow other users
* Recipe estimated prep time
## Credits
Fork icon from Freepik www.flaticon.com