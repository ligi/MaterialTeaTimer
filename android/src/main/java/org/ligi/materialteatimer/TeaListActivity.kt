package org.ligi.materialteatimer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_tealist.*

class TeaListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tealist)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tea_recycler.adapter = TeaAdapter()
        tea_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId){
        android.R.id.home -> {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
