package com.example.cookingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar

class RecipeSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText: EditText = view.findViewById(R.id.recipe_searchbar_edittext)
        val searchRatingBar: RatingBar = view.findViewById(R.id.recipe_searchbar_rating)
        val searchButton: Button = view.findViewById(R.id.recipe_searchbar_button)

        searchButton.setOnClickListener {
            val searchText = searchEditText.text.toString()
            val searchRating = searchRatingBar.rating

            val recipeListFragment =  (requireActivity() as MainActivity).supportFragmentManager.findFragmentByTag("recipeListFragmentTag") as? RecipeListFragment
            if(recipeListFragment != null){
                recipeListFragment.filterRecipes(searchText, searchRating)
                recipeListFragment.refreshData()
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_panel, recipeListFragment, "recipeListFragmentTag")
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}