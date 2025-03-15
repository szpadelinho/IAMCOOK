package com.example.cookingapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeListFragment : Fragment() {
    val itemList = mutableListOf<Item>()
    private lateinit var adapter: MyAdapter
    private var dataSender: FragmentDataSender? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_RECIPES = "recipes"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentDataSender){
            dataSender = context
        }
        else{
            throw RuntimeException("$context must implement FragmentDataSender")
        }
        sharedPreferences = context.getSharedPreferences("RecipesPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRecipes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.item_list_recipe)

        adapter = MyAdapter(itemList){item, position ->
            val bundle = Bundle()
            bundle.putString("name", item.name)
            bundle.putString("diff", item.difficulty)
            bundle.putInt("time", item.time.toInt())
            bundle.putFloat("rating", item.rating)
            bundle.putString("desc", item.description)

            val recipeDetailsFragment = RecipeDetailsFragment()
            recipeDetailsFragment.arguments = bundle

            (requireActivity() as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_panel, recipeDetailsFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    fun addItem(item: Item){
        itemList.add(item)
        saveRecipes()
        refreshData()
    }

    fun refreshData(){
        adapter.notifyDataSetChanged()
    }

    fun removeItem(name: String?){
        itemList.removeAll {it.name == name}
        saveRecipes()
        refreshData()
    }

    private fun loadRecipes(){
        val json = sharedPreferences.getString(PREF_RECIPES, null)
        if(json != null) {
            val type = object : TypeToken<List<Item>>() {}.type
            itemList.addAll(Gson().fromJson(json, type))
        }
    }

    private fun saveRecipes(){
        val json = Gson().toJson(itemList)
        sharedPreferences.edit().putString(PREF_RECIPES, json).apply()
    }
}