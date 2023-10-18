package com.app.drawingapp

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.drawingapp.ui.theme.DrawingAppTheme
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var drawingView: DrawingView
    private lateinit var brushButton:ImageButton

    private lateinit var purpleButton:ImageButton
    private lateinit var redButton:ImageButton
    private lateinit var blueButton:ImageButton
    private lateinit var greenButton:ImageButton
    private lateinit var orangeButton:ImageButton
    private lateinit var redoButton: ImageButton
    private lateinit var colorPickerButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView=findViewById(R.id.drawingView)
        purpleButton=findViewById(R.id.colorPickerPurple)
        redButton=findViewById(R.id.colorPickerRed)
        blueButton=findViewById(R.id.colorPickerBlue)
        greenButton=findViewById(R.id.colorPickerGreen)
        orangeButton=findViewById(R.id.colorPickerOrange)
        redoButton=findViewById(R.id.redoButton)
        colorPickerButton=findViewById(R.id.colorPicker)

        drawingView.changeBrushSize(23.toFloat())
        brushButton=findViewById(R.id.brushSelectionButton)
        brushButton.setOnClickListener{
            showBrushSizeSelection()
        }
        purpleButton.setOnClickListener(this)
        redButton.setOnClickListener(this)
        blueButton.setOnClickListener(this)
        greenButton.setOnClickListener(this)
        orangeButton.setOnClickListener(this)
        redoButton.setOnClickListener(this)
        colorPickerButton.setOnClickListener{
            changeColorToPicker()
        }

    }

    private fun changeColorToPicker(){
        val dialog=AmbilWarnaDialog(this,Color.BLUE,object:OnAmbilWarnaListener{
            override fun onCancel(dialog: AmbilWarnaDialog?) {

            }
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                drawingView.setColor(color)
            }
        })
        dialog.show()
    }

    private fun showBrushSizeSelection(){
        val brushDialog=Dialog(this@MainActivity)
        brushDialog.setContentView(R.layout.dialog_brush)
        val seekBarProgress=brushDialog.findViewById<SeekBar>(R.id.seekBar)
        val seekBarTextView=brushDialog.findViewById<TextView>(R.id.seekBarTV)

        seekBarProgress.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    drawingView.changeBrushSize(seekBar.progress.toFloat())
                    seekBarTextView.text=seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        brushDialog.show()
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.colorPickerPurple -> {
                drawingView.setColor("#270558")
            }
            R.id.colorPickerRed -> {
                drawingView.setColor("#E71046")
            }
            R.id.colorPickerBlue -> {
                drawingView.setColor("#0A40F1")
            }
            R.id.colorPickerGreen -> {
                drawingView.setColor("#0EE70A")
            }
            R.id.colorPickerOrange -> {
                drawingView.setColor("#EB6743")
            }
            R.id.redoButton -> {
                drawingView.redoPath()
            }
        }
    }
}
