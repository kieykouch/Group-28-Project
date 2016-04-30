package com.group28.cs160.shared;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by eviltwin on 4/26/16.
 * Modified by Haojun 4/29/16.
 * This is a class that contains all the nutritional facts. To access each nutritional
 * fact, you have to call the getAmount method and pass in a Nutrient type.
 * All Nutrient types are in the enum class Nutrient.
 */
public class NutritionFacts implements Serializable {
    public String name;
    public String fatSecretId;
    public String id;
    public String branch;
    public String dis;
    public double calories;  // measured in kcals. Standard diet is 2000kcals.
    public double protein;  // measured in grams.
    public double fiber;  // measured in grams.
    public double calcium;  // measured in milligrams.
    public double iron;  // measured in milligrams.
    public double potassium;  // measured in milligrams.
    public double vitaminC;
    public String serving;
    public boolean addedNutrients = false;

    public static NutritionFacts fromJson(JSONObject nutrition_facts) {
        return null;
    }

    /** Haojun uses this to create a nutrient fact. */
    public static NutritionFacts fromHashMap(String name, Map<String, Integer> map) {
        NutritionFacts facts = new NutritionFacts(name, (double) map.get("calories"));
        for (String key : map.keySet()) {
            switch (key) {
                case "protein":
                    facts.addAndReplace(Nutrient.PROTEIN, (double) map.get(key));
                    break;
                case "fiber":
                    facts.addAndReplace(Nutrient.FIBER, (double) map.get(key));
                    break;
                case "calcium":
                    facts.addAndReplace(Nutrient.CALCIUM, (double) map.get(key));
                    break;
                case "iron":
                    facts.addAndReplace(Nutrient.IRON, (double) map.get(key));
                    break;
                case "potassium":
                    facts.addAndReplace(Nutrient.POTASSIUM, (double) map.get(key));
                    break;
                case "vitaminC":
                    facts.addAndReplace(Nutrient.VITAMINC, (double) map.get(key));
                    break;
                default:
                    break;
            }
        }
        return facts;
    }
    public NutritionFacts(String name, double calories){
        this.name = name;
        this.calories = calories;
    }

    public NutritionFacts(String name, String branch, String fatSecretId, String dis){
        this.name = name;
        this.branch = branch;
        this.fatSecretId = fatSecretId;
        this.dis = dis;
    }

    /** To add a nutrient, call this method.
     * For example nutrientFacts.addAndReplace(Nutrient.PROTEIN, 1.0)
     * @param nutrient - the nutrient to be added. Use enum class variables like Nutrient.PROTEIN
     * @param amount - the amount to be added.
     */
    public void addAndReplace(Nutrient nutrient, double amount) {
        switch (nutrient) {
            case CALORIES:
                calories = amount;
                break;
            case PROTEIN:
                protein = amount;
                break;
            case FIBER:
                fiber = amount;
                break;
            case VITAMINC:
                vitaminC = amount;
                break;
            case CALCIUM:
                calcium = amount;
                break;
            case IRON:
                iron = amount;
                break;
            case POTASSIUM:
                potassium = amount;
                break;
            default:
                break;
        }
    }

    /** To get the amount of a nutrient call this method.
     * For example: nurientFacts.getAmount(Nutrient.PROTEIN)
     *
     * @param nutrient - the nutrient value you want
     */
    public double getAmount(Nutrient nutrient) {
        switch (nutrient) {
            case CALORIES:
                return calories;
            case PROTEIN:
                return protein;
            case FIBER:
                return fiber;
            case VITAMINC:
                return vitaminC;
            case CALCIUM:
                return calcium;
            case IRON:
                return iron;
            case POTASSIUM:
                return potassium;
            default:
                return 0;
        }
    }

    public String getName() {
        return name;
    }

    public void add(NutritionFacts n) {
        calories += n.calories;
        protein += n.protein;
        fiber += n.fiber;
        calcium += n.calcium;
        iron += n.iron;
        potassium += n.potassium;
        vitaminC += n.vitaminC;
    }

    // Implement serialization.
    // Do not change or delete this string. It will make future versions incompatible with old data.
    private static final long serialVersionUID = 7526472295622776147L;
    public static byte[] serialize(NutritionFacts nutritionFacts) {
        return SerializationUtils.serialize(nutritionFacts);
    }

    public static NutritionFacts deserialize(byte[] rep_array) {
        return (NutritionFacts) SerializationUtils.deserialize(rep_array);
    }

    public static String nutrientToString(Nutrient nutrient) {
        switch (nutrient) {
            case CALORIES:
                return "Calories";
            case PROTEIN:
                return "Protein";
            case FIBER:
                return "Fiber";
            case VITAMINC:
                return "Vitamin C";
            case CALCIUM:
                return "Calcium";
            case IRON:
                return "Iron";
            case POTASSIUM:
                return "Potassium";
            default:
                break;
        }
        return null;
    }

    public static int nutrientToResource(Nutrient nutrient) {
        switch (nutrient) {
            case CALORIES:
                return R.drawable.calories;
            case PROTEIN:
                return R.drawable.protein;
            case FIBER:
                return R.drawable.fiber;
            case VITAMINC:
                return R.drawable.vitaminc;
            case CALCIUM:
                return R.drawable.calcium;
            case IRON:
                return R.drawable.iron;
            case POTASSIUM:
                return R.drawable.potassium;
            default:
                break;
        }
        return -1;
    }

    public enum Nutrient {
        CALORIES, PROTEIN, FIBER, VITAMINC, CALCIUM, IRON, POTASSIUM
    }
}
