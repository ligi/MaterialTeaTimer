package org.ligi.materialteatimer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class TeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val teaName = itemView.findViewById(R.id.tea_name) as TextView
    val teaImage = itemView.findViewById(R.id.teaImage) as ImageView
    val infoIcon = itemView.findViewById(R.id.infoIcon) as ImageView?
    val minutes = itemView.findViewById(R.id.tea_mins) as TextView
    val seconds = itemView.findViewById(R.id.tea_secs) as TextView
    val temperature = itemView.findViewById(R.id.tea_temp) as TextView

    fun bind(teaInfo: TeaInfo) {
        teaName.text = teaInfo.name
        teaImage.setImageResource(teaInfo.imageRes)
        minutes.text = (teaInfo.brewTime / 60).toString() + "m"
        seconds.text = (teaInfo.brewTime % 60).toString() + "s"

        var temp = teaInfo.temp.toString()
        if (teaInfo.tempMax > 0) {
            temp += ".." + teaInfo.tempMax
        }
        temperature.text = temp + "°C"
        infoIcon?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(teaInfo.url)
            itemView.context.startActivity(intent)
        }

        itemView.setOnClickListener {
            Timer.resetAndPause()
            TeaProvider.currentTea = teaInfo
            (itemView.context as Activity).finish()
        }
    }
}