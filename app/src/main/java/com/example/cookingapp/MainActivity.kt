package com.example.cookingapp

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), FragmentDataSender {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if(savedInstanceState == null){
            Log.d("MainActivity", "Adding RecipeListFragment")
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_panel, RecipeListFragment(), "recipeListFragmentTag")
                .commit()
            Log.d("MainActivity", "RecipeListFragment added")
        }

        val addRecipeButton: ImageButton = findViewById(R.id.new_recipe_button)
        addRecipeButton.setOnClickListener {
            val addRecipeFragment = AddRecipeFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_panel, addRecipeFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onRecipeSelected(recipeName: String) {
        Log.i("IMPORT", "The data is being loaded...")
    }
}