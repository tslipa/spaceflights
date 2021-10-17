package solvro.spaceflights

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

import androidx.core.content.ContextCompat

import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.info_foreground)
        findViewById<Toolbar>(R.id.toolbar).overflowIcon = drawable
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info -> {
                val builder: AlertDialog.Builder = this.let {
                    AlertDialog.Builder(it)
                }
                builder.setMessage(R.string.app_info_message)
                    .setTitle(R.string.app_info)
                    .setPositiveButton(R.string.ok) { _, _ -> }
                builder.create().show()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}