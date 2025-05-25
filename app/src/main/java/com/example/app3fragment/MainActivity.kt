package com.example.app3fragment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.app3fragment.fragments.FragmentSector

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragment = FragmentSector.newInstance("Sectors")
        this.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment).commit()
    }
}