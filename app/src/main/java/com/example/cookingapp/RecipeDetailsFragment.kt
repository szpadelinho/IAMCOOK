package com.example.cookingapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class RecipeDetailsFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeNameTextView: TextView = view.findViewById(R.id.fragment_recipe_name)
        val recipeDiffTextView: TextView = view.findViewById(R.id.fragment_recipe_diff)
        val recipeTimeTextView: TextView = view.findViewById(R.id.fragment_recipe_time)
        val recipeDescTextView: TextView = view.findViewById(R.id.fragment_recipe_desc)
        val recipeRatingBar: RatingBar = view.findViewById(R.id.fragment_recipe_rating)
        val deleteRecipe: Button = view.findViewById(R.id.delete_recipe_button)

        arguments?.let{
            recipeNameTextView.text = it.getString("name")
            recipeDiffTextView.text = it.getString("diff")
            recipeTimeTextView.text = it.getInt("time").toString()
            recipeDescTextView.text = it.getString("desc")
            recipeRatingBar.rating = it.getFloat("rating")
        }

        deleteRecipe.setOnClickListener {
            val recipeListFragment = (requireActivity() as MainActivity).supportFragmentManager.findFragmentByTag("recipeListFragmentTag") as? RecipeListFragment
            if(recipeListFragment != null){
                recipeListFragment.removeItem(recipeNameTextView.text.toString())
                Toast.makeText(context, "Przepis usunięty!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
            else{
                Toast.makeText(context, "Błąd podczasusuwania przepisu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}