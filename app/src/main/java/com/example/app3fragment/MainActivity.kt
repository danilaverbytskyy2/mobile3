package com.example.app3fragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.app3fragment.fragments.FragmentLabel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragment = FragmentLabel.newInstance("Labels")
        this.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).commit()
    }
}