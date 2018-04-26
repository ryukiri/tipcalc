package austinqq.washington.edu.tipcalculator

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.amount_editText)
        val buttonTip = findViewById<Button>(R.id.tip_button)

        buttonTip.isEnabled = false

        fun EditText.addCurrencyFormatter() {
            this.addTextChangedListener(object: TextWatcher {

                private var current = ""

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (s.toString() != current) {
                        this@addCurrencyFormatter.removeTextChangedListener(this)
                        // strip off the currency symbol

                        // Reference for this replace regex: https://stackoverflow.com/questions/5107901/better-way-to-format-currency-input-edittext/28005836#28005836
                        val cleanString = s.toString().replace("\\D".toRegex(), "")
                        val parsed = if (cleanString.isBlank()) 0.0 else cleanString.toDouble()
                        // format the double into a currency format
                        val formated = NumberFormat.getCurrencyInstance()
                                .format(parsed / 100)

                        current = formated
                        this@addCurrencyFormatter.setText(formated)
                        this@addCurrencyFormatter.setSelection(formated.length)

                        this@addCurrencyFormatter.addTextChangedListener(this)
                        buttonTip.isEnabled = true

                    }
                }
            })

        }
        editText.addCurrencyFormatter()

        buttonTip.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var value = findViewById<EditText>(R.id.amount_editText) as EditText
                var finalVal = value.text.toString().removePrefix("$").toDouble() * .15
                Toast.makeText(this@MainActivity, "$" + finalVal.roundTo2DecimalPlaces().toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun Double.roundTo2DecimalPlaces() =
            BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
}
