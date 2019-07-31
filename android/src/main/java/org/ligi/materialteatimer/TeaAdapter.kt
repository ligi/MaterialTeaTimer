package org.ligi.materialteatimer

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class TeaAdapter : RecyclerView.Adapter<TeaViewHolder>() {

    override fun onBindViewHolder(holder: TeaViewHolder, position: Int)
            = holder.bind(TeaProvider.teas[position])

    override fun getItemCount() = TeaProvider.teas.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeaViewHolder
            = TeaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tea_card, parent, false))

}