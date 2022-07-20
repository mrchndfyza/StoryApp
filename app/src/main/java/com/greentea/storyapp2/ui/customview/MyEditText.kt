package com.greentea.storyapp2.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns.*
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.greentea.storyapp2.R

class MyEditText: AppCompatEditText, View.OnTouchListener {
    private lateinit var deleteButton: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context: Context, attributes: AttributeSet): super(context, attributes) {
        init()
    }

    constructor(
        context: Context,
        attributes: AttributeSet,
        defStyleAttributes: Int): super(context, attributes, defStyleAttributes){
            init()
        }

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        if(compoundDrawables[2] != null){
            val deleteButtonStart: Float
            val deleteButtonEnd: Float
            var isDeleteButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL){
                deleteButtonEnd = (deleteButton.intrinsicWidth + paddingStart).toFloat()
                when{
                    p1.x < deleteButtonEnd -> isDeleteButtonClicked = true
                }
            } else{
                deleteButtonStart = (width - paddingEnd - deleteButton.intrinsicWidth).toFloat()
                when{
                    p1.x > deleteButtonStart -> isDeleteButtonClicked = true
                }
            }

            if(isDeleteButtonClicked){
                when(p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        deleteButton = ContextCompat
                            .getDrawable(context, R.drawable.ic_round_delete) as Drawable
                        showDeleteButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        deleteButton = ContextCompat
                            .getDrawable(context, R.drawable.ic_round_delete) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        showDeleteButton()
                        return true
                    }
                    else -> return false
                }
            } else{
                return false
            }
        }
        return false
    }

    private fun showDeleteButton(){
        setButtonDrawables(endOfTheText = deleteButton)
    }

    private fun hideDeleteButton(){
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun init(){
        deleteButton =
            ContextCompat.getDrawable(context, R.drawable.ic_round_delete) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().isNotEmpty()){
                    showDeleteButton()
                    if(p0.toString().length < 6 && (inputType-1) == TYPE_TEXT_VARIATION_PASSWORD){
                        error = "Minimal berisi 6 character"
                    }else if(!EMAIL_ADDRESS.matcher(p0.toString()).matches() && (inputType-1)
                        == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS){
                        error = "Input email salah"
                    }
                } else{
                    error = "Perlu diisi"
                    hideDeleteButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //DO NOTHING
            }
        })
    }
}