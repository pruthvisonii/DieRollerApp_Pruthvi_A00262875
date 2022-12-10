package com.example.dierollerapp_pruthvi

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.regex.Pattern


class MainActivity() : AppCompatActivity() {
    private var rand: Random? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rand = Random()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun onClickClear(view: View?) {
        val logListing = findViewById<View>(R.id.LogListing) as TextView
        logListing.text = ""
    }

    fun onClickMarker(view: View?) {
        val logListing = findViewById<View>(R.id.LogListing) as TextView
        logListing.append("-------\n")
    }

    fun onClickRoll(view: View) {
        val button = view as Button
        val buttonText = button.text
        val logListing = findViewById<View>(R.id.LogListing) as TextView
        val roll = getNextRoll(buttonText, rand)
        logListing.append(
            buttonText.toString() + ": "
                    + (roll ?: "failure") + "\n"
        )
    }

    fun onClickAddNew(view: View?) {
        val rollTypeText = findViewById<View>(R.id.NewRollType) as EditText
        val newRollType: CharSequence = rollTypeText.text
        val m = rollPat.matcher(newRollType)
        if (m.find()) {
            // correct pattern, add new button with the pattern as text if unique
            val customRolls = findViewById<View>(R.id.CustomRollTypes) as LinearLayout
            var uniqueRollType = true
            for (i in 0 until customRolls.childCount) {
                if (((customRolls.getChildAt(i) as Button).text.toString() == newRollType.toString())) {
                    uniqueRollType = false
                }
            }
            val basicRollTypes = findViewById<View>(R.id.BasicRollTypes) as LinearLayout
            for (i in 0 until basicRollTypes.childCount) {
                if (((basicRollTypes.getChildAt(i) as Button).text.toString() == newRollType.toString())) {
                    uniqueRollType = false
                }
            }
            if (uniqueRollType) {
                val newBut = Button(customRolls.context)
                newBut.text = newRollType
                newBut.setOnClickListener { view2 -> onClickRoll(view2) }
                customRolls.addView(newBut)
            }
        } else {
            // something went wrong
        }
        rollTypeText.setText("")
    }

    companion object {
        private val rollPat = Pattern.compile("(\\d*)d(\\d+)([+-]\\d+)?")
        fun getNextRoll(buttonText: CharSequence?, r: Random?): Int? {
            val m = rollPat.matcher(buttonText)
            if (m.find()) {
                var multString = m.group(1)
                var dieString = m.group(2)
                var modString = m.group(3)
                if (multString == null || (multString == "")) {
                    multString = "1"
                }
                if (dieString == null) {
                    dieString = "1"
                }
                if (modString == null) {
                    modString = "0"
                }
                val multiplier = multString.toInt()
                val dieType = dieString.toInt()
                val modifier = modString.toInt()
                var total = 0
                for (i in 0 until multiplier) {
                    total += r!!.nextInt(dieType) + 1
                }
                total += modifier
                return total
            } else {
                return null
            }
        }
    }
}