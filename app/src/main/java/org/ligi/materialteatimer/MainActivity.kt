package org.ligi.materialteatimer

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.ligi.materialteatimer.R.drawable.vectalign_animated_vector_drawable_end_to_start


data class TeaInfo(val name: String, @DrawableRes val imageRes: Int, val brewTime: Int, val temp: Int, val tempMax: Int, val url: String)

class TeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val teaName = itemView.findViewById(R.id.tea_name) as TextView
    val teaImage = itemView.findViewById(R.id.teaImage) as ImageView
    val infoIcon = itemView.findViewById(R.id.infoIcon) as ImageView
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
        infoIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(teaInfo.url)
            itemView.context.startActivity(intent)
        }
    }
}

class TeaAdapter : RecyclerView.Adapter<TeaViewHolder>() {
    val teaList = arrayOf(
            TeaInfo("Green Tea", R.drawable.greentea, 2 * 60 + 30, 81, 87, "https://en.wikipedia.org/wiki/Green_tea"),
            TeaInfo("Black Tea", R.drawable.blacktea, 3 * 60 + 30, 100, 0, "https://en.wikipedia.org/wiki/Black_tea"),
            TeaInfo("Oolong", R.drawable.oolong, 4 * 60, 85, 95, "https://en.wikipedia.org/wiki/Oolong"),
            TeaInfo("Rooibos", R.drawable.rooibos, 7 * 60, 100, 0, "https://en.wikipedia.org/wiki/Rooibos"),
            TeaInfo("Peppermint", R.drawable.peppermint, 3 * 60, 90, 95, "https://en.wikipedia.org/wiki/Peppermint")
    )

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

class MainActivity : AppCompatActivity() {

    internal var isTiming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play_pause.setOnClickListener {
            isTiming = !isTiming

            val drawable = getDrawable(if (!isTiming) vectalign_animated_vector_drawable_end_to_start else R.drawable.vectalign_animated_vector_drawable_start_to_end) as AnimatedVectorDrawable
            play_pause.setImageDrawable(drawable)
            drawable.start()
        }

        tea_recycler.layoutManager = LinearLayoutManager(this)
        tea_recycler.adapter = TeaAdapter()
    }
}