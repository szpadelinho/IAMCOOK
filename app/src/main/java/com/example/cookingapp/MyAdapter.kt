package com.example.cookingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (
    private val itemList: MutableList<Item>,
    private val onItemClickListener: (Item, Int) -> Unit
): RecyclerView.Adapter<MyAdapter.ItemViewHolder>(){
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemName: TextView = itemView.findViewById(R.id.item_list_recipe_name)
        val itemDiff: TextView = itemView.findViewById(R.id.item_list_recipe_diff)
        val itemTime: TextView = itemView.findViewById(R.id.item_list_recipe_time)
        val itemRating: RatingBar = itemView.findViewById(R.id.item_list_recipe_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.name
        holder.itemDiff.text = currentItem.difficulty
        holder.itemTime.text = currentItem.time.toString()
        holder.itemRating.rating = currentItem.rating

        holder.itemView.setOnClickListener{
            val currentItem = itemList[position]
            val bundle = Bundle()
            bundle.putString("name", currentItem.name)
            bundle.putString("diff", currentItem.difficulty)
            bundle.putInt("time", currentItem.time.toInt())
            bundle.putFloat("rating", currentItem.rating)
            bundle.putString("desc", currentItem.description)

            val recipeDetailsFragment = RecipeDetailsFragment()
            recipeDetailsFragment.arguments = bundle

            (holder.itemView.context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_panel, recipeDetailsFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    override fun getItemCount(): Int = itemList.size
}