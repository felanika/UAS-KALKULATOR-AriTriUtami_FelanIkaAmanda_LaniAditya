package com.example.projekuas

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function

class MainActivity : AppCompatActivity() {
    private lateinit var solutionTv: TextView
    private lateinit var resultTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        solutionTv = findViewById(R.id.solution_tv)
        resultTv = findViewById(R.id.result_tv)

        val buttonIds = listOf(
            R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
            R.id.button_8, R.id.button_9, R.id.button_comma, R.id.button_ac,
            R.id.button_brackets, R.id.button_percent, R.id.button_divide,
            R.id.button_multiply, R.id.button_minus, R.id.button_plus,
            R.id.button_equals, R.id.button_sqrt, R.id.button_pi,
            R.id.button_power, R.id.button_factorial
        )

        buttonIds.forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener { handleButtonClick(id) }
        }
    }

    private fun handleButtonClick(id: Int) {
        when (id) {
            R.id.button_ac -> {
                solutionTv.text = ""
                resultTv.text = "0"
            }
            R.id.button_equals -> {
                calculateResult()
            }
            R.id.button_sqrt -> {
                solutionTv.append("sqrt(")
            }
            R.id.button_pi -> {
                solutionTv.append("pi")
            }
            R.id.button_power -> {
                solutionTv.append("^")
            }
            R.id.button_factorial -> {
                solutionTv.append("factorial(")
            }
            R.id.button_brackets -> {
                val currentText = solutionTv.text.toString()
                if (currentText.count { it == '(' } > currentText.count { it == ')' }) {
                    solutionTv.append(")")
                } else {
                    solutionTv.append("(")
                }
            }
            else -> {
                val button = findViewById<MaterialButton>(id)
                solutionTv.append(button.text)
            }
        }
    }

    private fun calculateResult() {
        var expression = solutionTv.text.toString()
        try {
            // Remove any trailing operators or incomplete expressions
            expression = expression.replace(Regex("[-+*/^]$"), "")

            // Tambahkan log untuk melihat ekspresi sebelum evaluasi
            Log.d("CalcDebug", "Expression before evaluation: $expression")

            // Define the factorial function
            val factorial = object : Function("factorial", 1) {
                override fun apply(vararg args: Double): Double {
                    val n = args[0]
                    if (n < 0) throw IllegalArgumentException("Negative factorials are not defined")
                    return (1..n.toInt()).fold(1.0) { acc, i -> acc * i }
                }
            }

            // Build the expression with the factorial function
            val exp = ExpressionBuilder(expression)
                .function(factorial)
                .build()

            // Evaluasi hasil ekspresi
            val result = exp.evaluate()

            // Tambahkan log untuk melihat hasil evaluasi
            Log.d("CalcDebug", "Result: $result")
            resultTv.text = result.toString()
        } catch (e: Exception) {
            Log.e("CalcError", "Error in calculation", e)
            resultTv.text = "Error"
        }
    }
}
