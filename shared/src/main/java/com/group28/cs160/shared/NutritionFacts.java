package com.group28.cs160.shared;

import org.apache.commons.lang3.SerializationUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by eviltwin on 4/26/16.
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
    public double vitaminc;
    public void fromJson(JSONObject nutrition_facts) {
        // TODO(haojun/ki): implement if necessary from an API.
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

    public void add(NutritionFacts n) {
        calories += n.calories;
        protein += n.protein;
        fiber += n.fiber;
        calcium += n.calcium;
        iron += n.iron;
        potassium += n.potassium;
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
}
