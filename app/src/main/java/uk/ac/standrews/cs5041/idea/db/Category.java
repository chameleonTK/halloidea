package uk.ac.standrews.cs5041.idea.db;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import uk.ac.standrews.cs5041.idea.R;

public class Category {
    public String name, colour;
    public Category(String name) {
        this.name = name;
        switch (name) {
            case "candy":
                this.colour = "#d28dd6";
                break;
            case "meal":
                this.colour = "#b3ea64";
                break;
            case "pumpkin":
                this.colour = "#ffc107";
                break;
            case "frankenstein":
                this.colour = "#4eb74d";
                break;
            case "mummy":
                this.colour = "#edeff1";
                break;
        }
    }

    public  static  String getColor(int viewId) {
        switch (viewId) {
            case R.id.category_candy:
                return "#d28dd6";
            case R.id.category_meal:
                return "#b3ea64";
            case R.id.category_pumpkin:
                return "#ffc107";
            case R.id.category_frankenstein:
                return "#4eb74d";
            case R.id.category_mummy:
                return "#edeff1";
            default:
                return "#000";
        }
    }

    public static Drawable getImage(int viewId, Context app) {
        switch (viewId) {
            case R.id.category_candy:
                return ContextCompat.getDrawable(app, R.drawable.ic_lollipop);
            case R.id.category_meal:
                return ContextCompat.getDrawable(app, R.drawable.ic_cauldron);
            case R.id.category_pumpkin:
                return ContextCompat.getDrawable(app, R.drawable.ic_pumpkin);
            case R.id.category_frankenstein:
                return ContextCompat.getDrawable(app, R.drawable.ic_frankenstein);
            case R.id.category_mummy:
                return ContextCompat.getDrawable(app, R.drawable.ic_mummy);
            default:
                return ContextCompat.getDrawable(app, R.drawable.ic_ghost);
        }
    }

    public static String getName(int viewId) {
        switch (viewId) {
            case R.id.category_candy:
                return "Candy";
            case R.id.category_meal:
                return "Meal";
            case R.id.category_pumpkin:
                return "Pumpkin";
            case R.id.category_frankenstein:
                return "Frankenstein";
            case R.id.category_mummy:
                return "Mummy";
            default:
                return "Unknown";
        }
    }
}
