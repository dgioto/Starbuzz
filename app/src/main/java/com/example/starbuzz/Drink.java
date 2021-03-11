package com.example.starbuzz;

public class Drink {
    private String name;
    private String description;
    private int imageResourceId;

    // drinks is an array of Drinks
    public static final Drink[] drinks = {
        new Drink("Latte", "A copule of espresso shot with steamed milk",
                R.drawable.latte),
        new Drink("Cappuccino", "Esprecco, hot milk, and a steamed milk foam",
                R.drawable.cappuccino),
        new Drink("Filter", "Hight quality beans roasted and brewed fresh",
                R.drawable.filter)
    };

    //Each Drink has a name, description, and an image resource
    private Drink(String name, String description, int imageResourceId){
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getDescription(){
        return description;
    }

    public String getName(){
        return  name;
    }

    public int getImageResourceId(){
        return imageResourceId;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
