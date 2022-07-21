package com.practice.unit_testing

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView0 = findViewById<TextView>(R.id.mars)
        val textView1 = findViewById<TextView>(R.id.the)

        val paint0 = textView0.paint
        val paint1 = textView1.paint

        val width0 = paint0.measureText(textView0.text.toString())
        val width1 = paint1.measureText(textView1.text.toString())

        val textShader0: Shader = LinearGradient(0f, 0f, width0, textView1.textSize, intArrayOf(
            Color.parseColor("#FC575E"),
            Color.parseColor("#F7B42C")
        ), null, Shader.TileMode.REPEAT)
        val textShader1: Shader = LinearGradient(0f, 0f, width1, textView1.textSize, intArrayOf(
            Color.parseColor("#FC575E"),
            Color.parseColor("#F7B42C")
        ), null, Shader.TileMode.REPEAT)

        textView0.paint.shader = textShader0
        textView1.paint.shader = textShader1

    }
}