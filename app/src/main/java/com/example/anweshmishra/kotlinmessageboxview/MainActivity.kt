package com.example.anweshmishra.kotlinmessageboxview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.messageboxview.MessageBoxView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MessageBoxView.create(this)
    }
}
