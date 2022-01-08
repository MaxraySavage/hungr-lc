# Hungr - a recipe sharing platform
Hungr is a web application for saving and sharing recipes between users.  It was built with Spring MVC, thymeleaf, Bulma, hibernate, and MySQL.

Hungr is deployed on Heroku [https://hungr-dev.herokuapp.com/](https://hungr-dev.herokuapp.com/) however the deployed version may not mirror the current version here in the repo.

## Goals
Create a way for people to easily save, share and try out new recipes without having to read a novel and fight through a bunch of popup advertisements.

## Progress
- [x] Brainstorm and build mock frontend
- [x] Build minimum viable product
- [x] Choose and complete first refinement (I chose to add ingredients)
- [x] Implement ingredients
- [x] Implement recipe steps
- [x] Implement user model
- [x] Build authentication and authorization
- [x] Add user profile pages
- [x] Give each user a favorite recipe list
- [x] Implement search
- [ ] Add more features

### Brainstorm front end
Spending some time thinking about how I want things to work eventually will help me to get my database design done well the first time.

### Minimum Viable Product
At absolute minimum I need to be able to perform CRUD operations on Recipes.
In order to get this done quickly, I created a simplified recipe object that simply consists of a name and short description.
From there I built the controller, data repository and thymeleaf templates and tested them manually.

### Refinements
A recipe is more than just a name and a description. After the Minimum Viable Product was created, I came up with the following list of possible refinements. 

* **Steps**
    * Recipes are broken up into steps. 
    * A recipe has a **One to Many** relationship with its steps. 
    * A step may need an index number (which number step is it?) and definitely needs text. 
    * In the future a step may have a time estimate or a picture associated with it.
* **Ingredients**
    * Maintaining a list of ingredients will allow users to search by what they have around the house and also build shopping lists more easily. 
    * Ingredients could have a **Many to Many** relationship with recipes. 
    * Are ingredients user created? This raises the possibility of misspelled ingredients etc. but solving for that could be challenging so we will start with allowing users to add ingredients. 
    * I actually decided not have a shared repository of ingredients between recipes for now. For now an ingredient exists in **Many to one** relationship with recipes and works exactly the same as recipe steps in the database. So an ingredient is something like "2 onions, chopped" and not "Onions" with some other field keeping track of preparation or amount
* **Time Estimates**
    * Recipes typically include an estimate of preparation time and active time
    * As each recipe only has one time estimate, I believe this would be best implemented as an added field on the Recipe object
    * Including time estimates for each recipe step could also be implemented
* **Tags/Type/Categories**
    * Recipes can be divided into types like *breakfast* or *side dish* or *one pot*. So we have to think, is this a **One to Many** relationship where a recipe can only have one type or category? Or can a recipe sometimes have more than one? 
    * If category and tags are really two different things then we should have both categories and tags.
    * Start off with a **Many to Many** tag for recipes. Then we can implement functionality to filter or search by tag. 
* **Users and Authentication**
    * This app becomes much more useful and fun once users are implemented.
    * A user should at least have a username and a password hash.
    * Once user authentication is implemented we'll have to think about what parts of the site are usable without authentication and how we should structure navigation.
* **Comments**
    * With Users added to the app, we may wish to add a comment section to each recipe so people can share their opinions about certain recipes 
    * A recipe has a **One to Many** relationship with its comments
    * A recipe comment has a **One to One** relationship with its author
* **Favorites**
    * Each user should be able to favorite a recipe and see a list of all their favorite recipes
#### Credits
fork icon from Freepik www.flaticon.com