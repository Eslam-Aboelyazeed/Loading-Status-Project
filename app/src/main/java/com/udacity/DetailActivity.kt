package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.concurrent.schedule

class DetailActivity : AppCompatActivity() {

    private lateinit var n: TextView
    private lateinit var v: TextView
    private lateinit var ml: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ml = findViewById(R.id.motion_layout)
        n = findViewById(R.id.status)
        v = findViewById(R.id.file_name)
        val p = findViewById<Button>(R.id.ok_button)

        Timer().schedule(100){ml.transitionToEnd()}

        p.setOnClickListener {
            ml.transitionToStart()
            this.finish()
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val e = intent?.extras

        n.text = e?.getString("status")
        v.text = e?.getString("file name")
    }

}
