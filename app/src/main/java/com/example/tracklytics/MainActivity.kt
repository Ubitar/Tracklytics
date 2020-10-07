package com.example.tracklytics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ubitar.tracklytics.TrackEvent
import com.ubitar.tracklytics.Tracklytics

class MainActivity : AppCompatActivity() {
    @TrackEvent("xxxx1")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Tracklytics.init {
            println(it)
        }
    }
}