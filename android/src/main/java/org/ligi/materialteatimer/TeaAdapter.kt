package org.ligi.materialteatimer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class TeaAdapter : RecyclerView.Adapter<TeaViewHolder>() {
    val teaList = TeaProvider.teas

    override fun onBindViewHolder(holder: TeaViewHolder, position: Int) {
        holder.bind(teaList[position])
    }

    override fun getItemCount(): Int {
        return teaList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeaViewHolder {
        return TeaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tea_card, parent, false))
    }

}