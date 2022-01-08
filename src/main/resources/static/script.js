  // As this js script will be loaded on every page (as of now) the code follows a certain pattern
  // After the page is loaded,
  // we query the document to see if there are any HTML elements that match a certain selector
  // usually a class
  // then if any of those elements are found, we add an event listener to each element we found
  // that creates the desired behavior
  // I've used a naming convention where a variable beginning with $ represents a dom element or an array of dom elements

document.addEventListener('DOMContentLoaded', () => {
  const parser = new DOMParser();

  // Query for all favorite buttons
  const $favoriteRecipeButtons = Array.from(document.querySelectorAll('.favorite-button'))

  //check to see if there are any favorite recipe buttons
  if($favoriteRecipeButtons.length > 0) {
    const favoriteBaseUrl = window.location.origin + '/users/favoriteRecipes/';

    // add event listeners to each favorite button
    $favoriteRecipeButtons.forEach( $favoriteButton => {
        // Get recipeId from targetRecipeId data field on button element
        const targetRecipeId = $favoriteButton.dataset.targetRecipeId;
        $favoriteButton.addEventListener('click', (event) => {
            event.preventDefault();

            const $favoriteButtonText =  $favoriteButton.querySelector('.favorite-button-text');

            const fetchMethod = $favoriteButton.dataset.isUserFavorite === 'true' ? 'DELETE' : 'POST';

            // Send HTTP POST request to favorite URL, credentials includes the session cookie
            fetch(favoriteBaseUrl + `?recipeId=${targetRecipeId}`, {
                method: fetchMethod,
                credentials: 'include',
            })
            .then((response) => {
                return response.text()
            }).then( text => {
                const $favoriteButtonIcon =  $favoriteButton.querySelector('.favorite-button-icon');
                const $favoriteButtonCount = $favoriteButton.querySelector('.favorite-button-count');

                // database response is of the form 'favorited recipe successfully' or 'unfavorited recipe successfully'
                // use response to switch button state as appropriate
                if(fetchMethod === 'POST'){
                    $favoriteButton.dataset.isUserFavorite = 'true';
                    $favoriteButton.classList.add('is-danger');
                    $favoriteButtonText.innerText = `Unfavorite`;
                    $favoriteButtonIcon.classList.remove('has-text-danger');
                    $favoriteButtonCount.innerText = parseInt($favoriteButtonCount.innerText, 10) + 1;
                } else if (fetchMethod === 'DELETE') {
                    $favoriteButton.dataset.isUserFavorite = 'false';
                    $favoriteButton.classList.remove('is-danger');
                    $favoriteButtonText.innerText = `Favorite`;
                    $favoriteButtonIcon.classList.add('has-text-danger');
                    $favoriteButtonCount.innerText = parseInt($favoriteButtonCount.innerText, 10) - 1;
                }
            })
        })
    });
  }

  // From stackoverflow
  // https://stackoverflow.com/questions/41137114/im-trying-to-use-hamburger-menu-on-bulma-css-but-it-doesnt-work-what-is-wron
  // Get all "navbar-burger" elements
  const $navbarBurgers = Array.from(document.querySelectorAll('.navbar-burger'));

  // Check if there are any navbar burgers
  if ($navbarBurgers.length > 0) {

    // Add a click event on each of them
    $navbarBurgers.forEach( el => {
      el.addEventListener('click', () => {

        // Get the target from the "data-target" attribute
        const target = el.dataset.target;
        const $target = document.getElementById(target);

        // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
        el.classList.toggle('is-active');
        $target.classList.toggle('is-active');

      });
    });
  }

  const resetPanelBlockInputIndexing = (panelBlockContainer) => {
    $panelInputs = Array.from(panelBlockContainer.querySelectorAll('input[type="checkbox"]'));
    $panelInputs.forEach( (el, index) => {
        // will produce weird behavior if el.name isn't exactly of the form 'text[index]'
        const nameArray = el.name.split('[');
        nameArray[1] = index;
        el.name = nameArray.join('[').concat(']');
    });
  }

  const panelBlockDeleteHandler = (event) => {
        event.preventDefault();
        const $button = event.target;
        const $parentDiv = $button.closest('.panel-column');
        const $panelBlockContainer = $button.closest('.columns');
        $parentDiv.remove();
        resetPanelBlockInputIndexing($panelBlockContainer);
  }

  const $panelBlockDeleteButtons = Array.from(document.querySelectorAll('.panel-block-delete'));
  if ($panelBlockDeleteButtons.length > 0) {
    $panelBlockDeleteButtons.forEach( el => {
      el.addEventListener('click', panelBlockDeleteHandler);
    })
  }


  const $panelInputButtons = Array.from(document.querySelectorAll('.is-panel-input-button'));
  if ($panelInputButtons.length > 0) {
    $panelInputButtons.forEach( el => {
      el.addEventListener('click', () => {
        const source = el.dataset.textSource;
        const $source = document.getElementById(source);

        const displayDestination = el.dataset.textDisplay;
        const $displayDestination = document.getElementById(displayDestination);

        const inputName = $source.dataset.name;
        const inputIndex = $displayDestination.querySelectorAll('input').length;
        const inputVal = $source.value;

        const newHTML = `<div class="column is-half panel-column">
                             <label class="panel-block">
                                    <span class="panel-icon panel-block-delete">
                                       <i class="fas fa-times-circle" aria-hidden="true"></i>
                                     </span>
                                     <input class="is-hidden" type="checkbox" name="${inputName}[${inputIndex}]" value="${inputVal}" checked>
                                     <span>${$source.value}</span>
                             </label>
                         </div>`;

        const newNodes = parser.parseFromString(newHTML, 'text/html');

        $newDeleteButton = newNodes.querySelector('.panel-block-delete');
        $newDeleteButton.addEventListener('click', panelBlockDeleteHandler);

        $displayDestination.appendChild(newNodes.body.firstChild);

        $source.value = '';
      })
    })
  }


  // this function deletes the closest parent dom element that matches .recipe-step
  // starting from event.target
  // and then renumbers the recipe steps accordingly
  const recipeStepDeleteHandler = (event) => {
      event.preventDefault();
      // we don't want to delete the step if there is only one on the page
      let $recipeSteps = Array.from(document.querySelectorAll('.recipe-step'));
      if($recipeSteps.length <= 1){
          return
      }
      // get the closest parent element that is a recipe-step and remove it
      const $parentDiv = event.target.closest('.recipe-step').remove();

      // update the numbering on the recipe steps
      $recipeSteps = Array.from(document.querySelectorAll('.recipe-step'));
      $recipeSteps.forEach( (el, index) => {
          el.querySelector('.recipe-step-number').innerText = `${index + 1}.`;
          el.querySelector('textarea').name = `steps[${index}]`;
      });
  }



  const $deleteRecipeStepButtons = document.querySelectorAll('.recipe-step-delete');

// add the recipe step delete handler to the recipe step delete buttons
  if ($deleteRecipeStepButtons.length > 0) {
    $deleteRecipeStepButtons.forEach( el => {
        el.addEventListener('click', recipeStepDeleteHandler);
    });
  }

  const $addRecipeStepButton = document.getElementById('add-recipe-step-button');

  if($addRecipeStepButton) {
    const $recipeStepContainer = document.getElementById('recipe-step-container');

    $addRecipeStepButton.addEventListener('click', (e) => {
        e.preventDefault()
        const stepIndex = Array.from($recipeStepContainer.querySelectorAll('.recipe-step')).length;

        const newHTML = `<article class="media recipe-step">
                                         <div class="media-left">
                                            <div class="control">
                                                <div class="block">
                                                    <span class="tag recipe-step-number">${stepIndex + 1}</span>
                                                </div>
                                                <button class="delete recipe-step-delete"></button>
                                            </div>
                                         </div>
                                         <div class="media-content">
                                             <div class="field">
                                                 <div class="control">
                                                 <textarea
                                                         class="textarea is-small step-input"
                                                         name="steps[${stepIndex}]"
                                                 ></textarea>
                                                 </div>
                                             </div>
                                         </div>
                                     </article>`;

        const newNodes = parser.parseFromString(newHTML, 'text/html');

        newNodes.querySelector('.recipe-step-delete').addEventListener('click', recipeStepDeleteHandler);

        $recipeStepContainer.appendChild(newNodes.body.firstChild);
    });
  }

  const $tabsContainers = document.querySelectorAll('.tabs');

  if($tabsContainers.length > 0) {
        $tabsContainers.forEach( tabsContainer => {
            const contentContainerId = tabsContainer.dataset.contentContainer;
            const $contentContainer = document.getElementById(contentContainerId);
            const $tabContentNodes = $contentContainer.querySelectorAll('.tab-content');
            

            const setActiveTabContent = (tabContentId) => {
              $tabContentNodes.forEach( tabContent => {
                  if(tabContent.id === tabContentId) {
                      tabContent.classList.remove('is-hidden');
                  } else {
                      tabContent.classList.add('is-hidden');
                  }
              });
            }

            const $tabArray = tabsContainer.querySelectorAll('a[data-tab-content]');

            const setActiveTab = (tabToActivate) => {
              $tabArray.forEach( tab => {
                if(tab.isSameNode(tabToActivate)) {
                  const contentToActivateId = tab.dataset.tabContent;
                  setActiveTabContent(contentToActivateId);
                  tab.closest('li').classList.add('is-active');
                } else {
                  tab.closest('li').classList.remove('is-active');
                }
              })
            }


            $tabArray.forEach( tab => {
                tab.addEventListener('click', event => {
                    event.preventDefault();
                    setActiveTab(tab);
                });
            });
        });
  }

  // Icon select display element

  const $recipeIconSelectElements = document.querySelectorAll('.recipe-icon-select-input');

  if($recipeIconSelectElements.length > 0){
    $recipeIconSelectElements.forEach(($iconSelectElement) => {
        $iconSelectElement.addEventListener('change', (event) => {
            const $iconDisplay = document.getElementById($iconSelectElement.dataset.target);
            $iconDisplay.className = $iconSelectElement.selectedOptions[0].dataset.iconClass;
        });

    });

  }


});