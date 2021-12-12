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
    const favoriteBaseUrl = window.location.origin + '/favorite/';

    // add event listeners to each favorite button
    $favoriteRecipeButtons.forEach( el => {
        el.addEventListener('click', (event) => {
            event.preventDefault();

            // Get recipeId from targetRecipeId data field on button element
            const targetRecipeId = el.dataset.targetRecipeId;

            // Send HTTP POST request to favorite URL, credentials includes the session cookie
            fetch(favoriteBaseUrl + `?recipeId=${targetRecipeId}`, {
                method: 'POST',
                credentials: 'include',
            })
            .then((response) => {
                return response.text()
            }).then( text => {
                const $favoriteButtonText =  el.querySelector('.favorite-button-text');
                const $favoriteButtonIcon =  el.querySelector('.favorite-button-icon');
                const $favoriteButtonCount = el.querySelector('.favorite-button-count');

                // database response is of the form 'favorited recipe successfully' or 'unfavorited recipe successfully'
                // use response to switch button state as appropriate
                if(text.startsWith('favorited')){
                    $favoriteButtonText.innerText = `Unfavorite`;
                    el.classList.add('is-danger');
                    $favoriteButtonIcon.classList.remove('has-text-danger');
                    $favoriteButtonCount.innerText = parseInt($favoriteButtonCount.innerText, 10) + 1;
                } else if (text.startsWith('unfavorited')) {
                    $favoriteButtonText.innerText = `Favorite`;
                    el.classList.remove('is-danger');
                    $favoriteButtonIcon.classList.add('has-text-danger');
                    $favoriteButtonCount.innerText = parseInt($favoriteButtonCount.innerText, 10) - 1;
                }
            })

        })


    });


  }

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

  const $tabsContainer = Array.from(document.querySelectorAll('.tabs'));

  if($tabsContainer.length > 0) {
        $tabsContainer.forEach( el => {
            const targetContainerId = el.dataset.target;
            const $targetContainer = document.getElementById(targetContainerId);
            const $targetTabs = Array.from($targetContainer.querySelectorAll('.target-tab'));

            const setActiveTargetTab = (targetTabId) => {
                $targetTabs.forEach( tab => {
                    if(tab.id === targetTabId) {
                        tab.classList.remove('is-hidden');
                    } else {
                        tab.classList.add('is-hidden');
                    }
                });
            }

            const $tabArray = el.querySelectorAll('a[data-target-tab]');
            $tabArray.forEach( tab => {
                const targetTabId = tab.dataset.targetTab;
                tab.addEventListener('click', event => {
                    event.preventDefault();
                    setActiveTargetTab(targetTabId);
                    $tabArray.forEach( el => {
                        el.closest('li').classList.remove('is-active');
                    })
                    tab.closest('li').classList.add('is-active');
                });
            });
        });
  }


});