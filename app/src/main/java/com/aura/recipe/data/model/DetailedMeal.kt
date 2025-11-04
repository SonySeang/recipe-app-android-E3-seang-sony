package com.aura.recipe.data.model

import com.google.gson.annotations.SerializedName

data class MealDetail(
    @SerializedName("id") val id: String,
    @SerializedName("meal") val mealName: String,
    @SerializedName("drinkAlternate") val drinkAlternate: String?,
    @SerializedName("category") val category: String,
    @SerializedName("area") val area: String,
    @SerializedName("instructions") val instructions: String,
    @SerializedName("mealThumb") val mealThumb: String,
    @SerializedName("tags") val tags: String?,
    @SerializedName("youtube") val youtube: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("ingredients") val ingredients: List<IngredientDetail>,
    @SerializedName("categoryId") val categoryId: String
)
data class IngredientDetail(
    @SerializedName("ingredient") val ingredient: String,
    @SerializedName("measure") val measure: String
)
// Remove@SerializedName("meals") val meals: List<MealDetail>)