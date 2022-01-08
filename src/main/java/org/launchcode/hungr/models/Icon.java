package org.launchcode.hungr.models;

public enum Icon {
    UTENSILS("fas fa-utensils", "Utensils"),
    PIZZA("fas fa-pizza-slice", "Pizza"),
    BACON("fas fa-bacon", "Bacon"),
    APPLE("fas fa-apple-alt", "Apple"),
    HAMBURGER("fas fa-hamburger", "Hamburger"),
    ICECREAM("fas fa-ice-cream", "Ice Cream"),
    HOTDOG("fas fa-hotdog", "Hot Dog"),
    FISH("fas fa-fish", "Fish"),
    DRUMSTICK("fas fa-drumstick-bite", "Drumstick"),
    CHEESE("fas fa-cheese", "Cheese"),
    BREAD("fas fa-bread-slice", "Bread"),
    COOKIE("fas fa-cookie-bite", "Cookie"),
    HOTPEPPER("fas fa-pepper-hot", "Hot Pepper"),
    WAFFLE("fas fa-stroopwafel", "Waffle"),
    LEMON("fas fa-lemon", "Lemon"),
    CARROT("fas fa-carrot", "Carrot");

    private String fontAwesomeClass;

    private String displayName;

    private Icon(String iconClass, String displayName){
        this.fontAwesomeClass = iconClass;
        this.displayName = displayName;
    }

    public String getFontAwesomeClass() {
        return fontAwesomeClass;
    }

    public String getDisplayName() {
        return displayName;
    }
}
