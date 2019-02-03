package com.anwesh.uiprojects.linkedellipticaledgeview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.ellipticaledgeview.EllipticalEdgeView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view : EllipticalEdgeView = EllipticalEdgeView.create(this)
    }
}
