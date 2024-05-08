package com.example.colorgame
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.colorgame.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var timerTextView: TextView
    private lateinit var colorTextView: TextView
    private lateinit var colorN: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timerSeconds = 5
    private var isProcessingClick = false
    private var isScoreUpdated = false

    private var changeCount = 0
    private var matchInterval = 2
    private var isDialogShown = false
    private val colorNames = arrayOf(
        "RED", "GREEN", "BLUE", "YELLOW", "CYAN", "MAGENTA", "GRAY",

    )
    private var score = 0
    private val colorList = listOf(
        Pair("RED", 0xFFFF0000.toInt()),
        Pair("RED", 0xFFFF0000.toInt()),
        Pair("GREEN", 0xFF00FF00.toInt()),
        Pair("GREEN", 0xFF00FF00.toInt()),
        Pair("BLUE", 0xFF0000FF.toInt()),
        Pair("BLUE", 0xFF0000FF.toInt()),
        Pair("YELLOW", 0xFFFFFF00.toInt()),
        Pair("YELLOW", 0xFFFFFF00.toInt()),
        Pair("CYAN", 0xFF00FFFF.toInt()),
        Pair("CYAN", 0xFF00FFFF.toInt()),
        Pair("MAGENTA", 0xFFFF00FF.toInt()),
        Pair("MAGENTA", 0xFFFF00FF.toInt()),
        Pair("GRAY", 0xFF808080.toInt()),
        Pair("GRAY", 0xFF808080.toInt()),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        timerTextView = findViewById(R.id.timer)
        colorN = findViewById(R.id.TextName)
        colorTextView = findViewById(R.id.colorText)
        scoreTextView = findViewById(R.id.score)

        startTimer()

    }



    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timerSeconds * 1000L, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                timerTextView.text = seconds.toString()
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                val (randomName, randomColor) = changeBackgroundColor()

                colorN.text = randomName
                colorTextView.setBackgroundColor(randomColor)

                Handler(Looper.getMainLooper()).postDelayed({
                    countDownTimer.cancel() // Cancel the current timer
                    startTimer() // Start a new timer
                }, 1000) // Delay to show the result before restarting the timer
            }
        }
        countDownTimer.start()
    }




    private fun resetTimer() {
        timerTextView.text = "00:00"
//        startTimer()
    }

    private fun changeBackgroundColor(): Pair<String, Int> {
        isScoreUpdated = false

        val randomColorIndex = Random.nextInt(0, colorList.size)
        val randomNameIndex = Random.nextInt(0, colorNames.size)
        val randomColor = colorList[randomColorIndex]
        val randomName = if (changeCount % matchInterval == 0) {
            getColorName(randomColor.second)
        } else {
            colorNames[randomNameIndex]
        }
        colorN.text = randomName
        colorTextView.setBackgroundColor(randomColor.second)

        changeCount++
        if (changeCount == matchInterval) {
            changeCount = 0
        }

        return Pair(randomName, randomColor.second)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()

    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !isProcessingClick) {
            isProcessingClick = true
            try {
                val colorText = colorTextView.text.toString()
                val colorName = colorN.text.toString()

                val colorValue =
                    (colorTextView.background as? ColorDrawable)?.color ?: Color.TRANSPARENT
                val colorNameFromValue = getColorName(colorValue)

                if (colorName.equals(colorNameFromValue, ignoreCase = true) && !isScoreUpdated) {
                    score++
                    scoreTextView.text = "Score: $score"
                    isScoreUpdated = true // Set flag to indicate score has been updated
                } else if (!isScoreUpdated) {
                    showResultDialog()
                    countDownTimer.cancel()
                    scoreTextView.text = "0"
                }
            } finally {
                isProcessingClick = false
            }

            Handler(Looper.getMainLooper()).postDelayed({
                isProcessingClick = false
            }, 500)
        }
        return super.onTouchEvent(event)
    }


    private fun showResultDialog() {
        if (!isDialogShown) {
            isDialogShown = true
            countDownTimer.cancel()
            resetTimer()

            val dialog = AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your score is $score")
                .setPositiveButton("OK") { _, _ ->
                    score = 0
                    timerTextView.text = "00:00"
                    startTimer()
                    isDialogShown = false
                }
                .setCancelable(false)
                .create()
            dialog.show()
        }
    }
    private fun getColorName(colorValue: Int): String {
        return colorList.find { it.second == colorValue }?.first ?: ""
    }

    override fun onResume() {
        super.onResume()
        countDownTimer.start()
    }
}