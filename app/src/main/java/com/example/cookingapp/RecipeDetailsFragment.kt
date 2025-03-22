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

    private var itemName: String? = null
    private var itemDiff: String? = null
    private var itemTime: Int = 0
    private var itemDesc: String? = null
    private var itemRating: Float = 0.0f

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
            itemName = it.getString("name")
            itemDiff = it.getString("diff")
            itemTime = it.getInt("time")
            itemDesc = it.getString("desc")
            itemRating = it.getFloat("rating")

            recipeNameTextView.text = itemName
            recipeDiffTextView.text = itemDiff
            recipeTimeTextView.text = itemTime.toString()
            recipeDescTextView.text = itemDesc
            recipeRatingBar.rating = itemRating
        }

        recipeRatingBar.setOnRatingBarChangeListener{ _, rating, _ ->
            val recipeListFragment = (requireActivity() as MainActivity).supportFragmentManager.findFragmentByTag("recipeListFragmentTag") as? RecipeListFragment
            if(recipeListFragment != null){
                val updatedItem = Item(itemName!!, itemDiff!!, itemTime, rating, itemDesc!!)
                recipeListFragment.updateItem(updatedItem)
                recipeListFragment.refreshData()
            }
        }

        deleteRecipe.setOnClickListener {
            val recipeListFragment = (requireActivity() as MainActivity).supportFragmentManager.findFragmentByTag("recipeListFragmentTag") as? RecipeListFragment
            if(recipeListFragment != null){
                recipeListFragment.removeItem(recipeNameTextView.text.toString())
                Toast.makeText(context, "Przepis usunięty!", Toast.LENGTH_SHORT).show()
                (requireActivity() as MainActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_panel, recipeListFragment, "recipeListFragmentTag")
                    .commit()
            }
            else{
                Toast.makeText(context, "Błąd podczasusuwania przepisu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}