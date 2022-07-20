package com.greentea.storyapp2.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.greentea.storyapp2.R

class MyButton : AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attributes: AttributeSet): super(context, attributes){
        init()
    }

    constructor(
        context: Context,
        attributes: AttributeSet,
        defStyleAttribute: Int)
            : super(context, attributes, defStyleAttribute){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = if (isEnabled){
            enabledBackground
        } else {
            disabledBackground
        }

        if (isEnabled){
            txtColor = ContextCompat.getColor(context, R.color.font_dark)
            setTextColor(txtColor)
        }
        else{
            txtColor = ContextCompat.getColor(context, R.color.font_light)
            setTextColor(txtColor)
        }

        isAllCaps = false

        textSize = 16f
        gravity = Gravity.CENTER
    }

    private fun init(){
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled)
                as Drawable
    }
}