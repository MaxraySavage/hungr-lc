document.addEventListener('DOMContentLoaded', () => {
  const parser = new DOMParser();

  const panelBlockDeleteHandler = (event) => {
      event.preventDefault();
      const $button = event.target;
      const $parentDiv = $button.closest('.panel-column');
      $parentDiv.remove();
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

        const newHTML = `<div class="column is-half panel-column">
                             <label class="panel-block">
                                    <span class="panel-icon panel-block-delete">
                                       <i class="fas fa-times-circle" aria-hidden="true"></i>
                                     </span>
                                     <input class="is-hidden" type="checkbox" name="${$source.dataset.name}" value="${$source.value}" checked>
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




});