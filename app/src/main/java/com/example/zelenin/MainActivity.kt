package com.example.zelenin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zelenin.databinding.ActivityMainBinding
import com.example.zelenin.fragments.DevelopersLifeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentsContainer,
                DevelopersLifeFragment(),
                DevelopersLifeFragment.TAG
            )
            .commit()
    }
}