package com.example.cookingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast

class AddRecipeFragment : Fragment() {
    private val itemList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addRecipe: Button = view.findViewById(R.id.add_recipe_button)
        val recipeName: EditText = view.findViewById(R.id.add_recipe_name)
        val recipeDiff: EditText = view.findViewById(R.id.add_recipe_diff)
        val recipeTime: EditText = view.findViewById(R.id.add_recipe_time)
        val recipeDesc: EditText = view.findViewById(R.id.add_recipe_desc)
        val recipeRating: RatingBar = view.findViewById(R.id.add_recipe_rating)

        addRecipe.setOnClickListener{
            val name = recipeName.text.toString()
            val diff = recipeDiff.text.toString()
            val timeChecker = recipeTime.text.toString()
            val time = timeChecker.toIntOrNull()
            val rating = recipeRating.rating
            val desc = recipeDesc.text.toString()

            if(name.isEmpty() || diff.isEmpty() || time == null || desc.isEmpty()){
                Toast.makeText(context, "Dane nie mogą być puste", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newItem = Item(name, diff, time, rating, desc)

            val recipeListFragment = (requireActivity() as MainActivity).supportFragmentManager.findFragmentByTag("recipeListFragmentTag") as? RecipeListFragment
            if(recipeListFragment != null){
                Log.d("AddRecipeFragment", "Fragment found")
                recipeListFragment.addItem(newItem)
                recipeListFragment.refreshData()
                Toast.makeText(context, "Przepis dodany!", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack()
            }
            else{
                Log.d("AddRecipeFragment", "Fragment not found")
                Toast.makeText(context, "Błąd dodawania przepisu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}