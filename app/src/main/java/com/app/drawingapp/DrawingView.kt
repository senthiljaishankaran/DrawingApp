package com.app.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.Path
import android.util.TypedValue
import android.view.MotionEvent

class DrawingView ( context: Context, attribute: AttributeSet): View(context,attribute) {

    // draw FingerPath
    // Here we define the path of the brush movement that are made by the user based on the canvas draw call
    private lateinit var drawPath:FingerPath

    // draw canvas
    // canvas provides the surface to draw in the app
    // it make the draw call and reflects the call functions ie drawn object to the UI
    private lateinit var canvas:Canvas

    // draw Paint
    // Paint defines the properties of the draw call made by the canvas
    // there are two paints
    // 1. draw paint - defines the properties like brush size and color(How to draw)
    // 2. canvas paint - defines the shape of the draw call made by the canvas(What to draw)
    // ie if we define a rectangle in the drawing the shape of rectangle is defined by canvas paint and the color is defined by draw paint
    private lateinit var drawPaint: Paint
    private lateinit var canvasPaint: Paint
    private var color=android.graphics.Color.BLACK
    private var brushSize:Float=0.toFloat()
    private var paths= mutableListOf<FingerPath>()


    // draw bitmap
    // defines the pixel of the draw call
    private lateinit var bitMap:Bitmap

    init{
        setupDrawing()
    }

    private fun setupDrawing(){
        drawPaint=Paint()
        drawPath=FingerPath(color, brushSize)
        drawPaint.color=color
        drawPaint.style=Paint.Style.STROKE
        drawPaint.strokeJoin=Paint.Join.ROUND
        drawPaint.strokeCap=Paint.Cap.ROUND
        brushSize=20.toFloat()
        canvasPaint=Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // here this function is to used to change the bitmap according to the changed with and height
        // And based upon this width and height we create bitmap
        // then using the bitmap we create a new canvas
        super.onSizeChanged(w, h, oldw, oldh)
        bitMap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas=Canvas(bitMap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw bitmap is used to draw the defined bitmap canvas to the View module
        // 0f is zero float for origin point of the draw bitmap
        // we are checking that if the draw path is not empty then we are assigning the color and brush thickness of drawPaint object using drawPath object
        // then the actual drawing is drawn using the canvas that is created with bitmap and the draw path
        canvas.drawBitmap(bitMap,0f,0f,drawPaint)

        if(!drawPath.isEmpty){
            drawPaint.color=drawPath.color
            drawPaint.strokeWidth=drawPath.brushThickness
            canvas.drawPath(drawPath,drawPaint)
        }
        // for loop the draw path in the paths mutable list to keep the draw paths retained
        for (path in paths) {
            drawPaint.color = path.color
            drawPaint.strokeWidth = path.brushThickness
            canvas.drawPath(path, drawPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX=event?.x
        val touchY=event?.y

        when(event?.action){
            // This action method is used when the user touches the screen
            // where we assign the color and brush thickness of draw path assigned at the setup drawing method
            // this will replace the initial values of color and brush thickness that is assigned initially
            // this will only change if the user changes the color and brush thickness
            MotionEvent.ACTION_DOWN->{
                drawPath.color=color
                drawPath.brushThickness=brushSize
                drawPath.reset()
                drawPath.moveTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_MOVE->{
                // this will record the x and y up to where the user moves the finger and draws line
                drawPath.lineTo(touchX!!,touchY!!)
            }
            MotionEvent.ACTION_UP->{
                // this will assign the given color and brush thickness by the user once the user release the finger from the touch
                drawPath=FingerPath(color,brushSize)
                // adding the drawPath to the paths mutable list
                paths.add(drawPath)
            }
            else -> return false
        }
        // this will reset the widget thus using all the action from the above to create the drawing
        invalidate()
        return true
    }
    
    fun changeBrushSize(newSize:Float){

        // here we are changing the brush size using TypedValue class using the applyDimension method
        // it converts the ComplexUnitDip of typed dimension to pixel dimension
        // it converts the given value to pixel dimension based on the resources provided on display metrics of the device
        brushSize=TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,newSize,
            resources.displayMetrics
        )
        drawPaint.strokeWidth=brushSize
    }

    fun setColor(newColor:Any){
        if(newColor is String){
            color=Color.parseColor(newColor)
            drawPaint.color=color
        }else{
            color=newColor as Int
            drawPaint.color=color
        }
    }

    fun redoPath(){
        if(paths.size > 0){
            paths.removeAt(paths.size-1)
            invalidate()
        }
    }

    internal inner class FingerPath(var color: Int, var brushThickness:Float): Path()

}