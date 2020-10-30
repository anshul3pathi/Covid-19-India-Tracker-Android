package com.example.covid19tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StateWiseListAdapter(): RecyclerView.Adapter<StateWiseViewHolder>() {

    private val items: ArrayList<StateData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateWiseViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.state_wise_item_view, parent, false)
        return StateWiseViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateWiseViewHolder, position: Int) {
        val currentItem = items[position]
        holder.stateNameText.text = currentItem.stateName.toString()
        holder.confirmedNumberRV.text = currentItem.confirmed.toString()
        holder.activeNumberRV.text = currentItem.active.toString()
        holder.recoveredNumberRV.text = currentItem.recovered.toString()
        holder.deceasedNumberRV.text = currentItem.deceased.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateCovidData(stateWiseData: ArrayList<StateData>) {
        items.clear()
        items.addAll(stateWiseData)
        notifyDataSetChanged()
    }

}

class StateWiseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val stateNameText: TextView = itemView.findViewById(R.id.stateNameTextView)
    val confirmedNumberRV: TextView = itemView.findViewById(R.id.confirmedNumberRV)
    val activeNumberRV: TextView = itemView.findViewById(R.id.activeNumberRV)
    val recoveredNumberRV: TextView = itemView.findViewById(R.id.recoveredNumberRV)
    val deceasedNumberRV: TextView = itemView.findViewById(R.id.deceasedNumberRV)
}