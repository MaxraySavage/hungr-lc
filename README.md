# hungr recipe app
A web application built with Spring MVC, thymeleaf, Bulma, hibernate, and MySQL.

Deployed on Heroku [https://hungr-dev.herokuapp.com/](https://hungr-dev.herokuapp.com/)
## Goals
Create a way for people to easily save, share and try out new recipes without having to read a novel and fight through a bunch of popup advertisements.

## Progress
- [x] Brainstorm and build mock frontend
- [x] Build minimum viable product
- [ ] Choose and complete first refinement

### Brainstorm front end
Spending some time thinking about how I want things to work eventually will help me to get my database design done well the first time.

### Minimum Viable Product
At absolute minimum I need to be able to perform CRUD operations on Recipes.
In order to get this done quickly, we can create a simplified recipe object that simply consists of a name and short description.
Then we can build the controller, data repository and thymeleaf templates to make that happen.

### Refinements
A recipe is more than just a name and a description.

#### Steps
Recipes are broken up into steps.
A recipe has a **One to Many** relationship with its steps.
A step has an index number (which number step is it?) and text.
In the future a step may have a time estimate.

#### Ingredients
Maintaining a list of ingredients will allow users to search by what they have around the house and also build shopping lists more easily.
Ingredients have a **Many to Many** relationship with recipes.
Are ingredients user created? i think so. This raises the possibility of misspelled ingredients etc. but solving for that could be challenging so we will start with allowing users to add ingredients.
Basic steps as I envision this would be to create an ingredient entity, an ingredient data repository,a thymeleaf fragment to display a recipe's ingredients, and a way to add ingredients to a recipe on creation and update. 

#### Time Estimates
One way we can help people out who are using a recipe is by giving a good estimate of how long each step may take.
A recipe can have a time estimate field.
We may wish for each step to have a time estimate field but there are complications that could arise where a computed estimate from the sum of recipe steps would differ significantly from the user input estimate for the reccipe as a whole.

We will start by simply adding a time estimate field to recipes and updating our front end to display time estimates.

#### Tags/Types/Categories
Recipes can be divided into types like *breakfast* or *side dish* or *one pot*.
So we have to think, is this a **One to Many** relationship where a recipe can only have one type or category?
Or can a recipe sometimes have more than one?
Are there really two different things going on and we should have both categories and tags?
We don't know. 

We can start with tags as a **Many to Many** data field.

Wwe will create the tag object, it's associated data repository and update frontend accordingly.

Then we should implement functionality to filter or search by tag.

#### Users and Authentication
This app becomes much more useful and fun once users are implemented.

A user should at least have a user name and a password hash. An email address would alos be good even if we aren't using email now.

Once user authentication is implemented we'll have to thin about what parts of the site are usable without authentication and how we should structure navigation.

### Road map
First step will be a minimum viable product.
From there I think including recipe steps will add the most value. After that, who knows!


#### Credits
fork icon from Freepik www.flaticon.com