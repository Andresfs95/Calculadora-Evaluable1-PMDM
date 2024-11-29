package com.example.holamundo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Variables principales
    private lateinit var display: TextView
    private var currentInput: String = ""
    private var lastValue: Int = 0
    private var lastOperator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincula el display
        display = findViewById(R.id.display)

        // Configura los listeners para los botones de dígitos (0-9)
        val digitButtons = listOf(
            R.id.cero, R.id.uno, R.id.dos, R.id.tres, R.id.cuatro,
            R.id.cinco, R.id.seis, R.id.siete, R.id.ocho, R.id.nueve
        )
        for (id in digitButtons) {
            findViewById<Button>(id)?.setOnClickListener { onDigit(it) }
        }

        // Configura los listeners para los botones de operadores
        val operatorButtons = listOf(R.id.sumar, R.id.restar, R.id.multiplicar, R.id.dividir)
        for (id in operatorButtons) {
            findViewById<Button>(id)?.setOnClickListener { onOperator(it) }
        }

        //Configuracion de listeners para otros botones de otros operadores
        findViewById<Button>(R.id.igual)?.setOnClickListener { onEquals(it) }
        findViewById<Button>(R.id.ac)?.setOnClickListener { onClear(it) }
        findViewById<Button>(R.id.delete)?.setOnClickListener { onDelete(it) }
        findViewById<Button>(R.id.porcentaje)?.setOnClickListener { onPercentage(it) }

        //Configuracion de Listeners para botones de metodos cientificos
        findViewById<Button>(R.id.seno)?.setOnClickListener { onScientificFunction(it) }
        findViewById<Button>(R.id.coseno)?.setOnClickListener { onScientificFunction(it) }
        findViewById<Button>(R.id.tangente)?.setOnClickListener { onScientificFunction(it) }
        findViewById<Button>(R.id.logaritmo)?.setOnClickListener { onScientificFunction(it) }
        findViewById<Button>(R.id.potencia)?.setOnClickListener { onScientificFunction(it) }

    }

    // Manejar clics en los botones de dígitos
    private fun onDigit(view: View) {
        val button = view as Button
        currentInput += button.text
        updateDisplay()
    }

    // Manejar clics en los botones de operadores
    private fun onOperator(view: View) {
        val button = view as Button
        if (currentInput.isNotEmpty()) {
            if (lastOperator.isNotEmpty()) {
                performCalculation()
            } else {
                lastValue = currentInput.toInt()
            }
        }
        lastOperator = button.text.toString()
        currentInput = ""
    }

    // Manejamos los clicks del boton =
    private fun onEquals(view: View) {
        if (currentInput.isNotEmpty() && lastOperator.isNotEmpty()) {
            performCalculation()
            lastOperator = "" // Limpiar operador después de calcular
        }
    }

    // Manejamos los clicks del boton de limpiar (AC)
    private fun onClear(view: View) {
        currentInput = ""
        lastValue = 0
        lastOperator = ""
        updateDisplay()
    }
    // Borra un digito de la calculadora
    private fun onDelete(view: View) {
        if (currentInput.isNotEmpty()) {
            // Elimina el último carácter de currentInput
            currentInput = currentInput.dropLast(1)
            updateDisplay()
        }
    }

    // Realizamos el calculo segun el operador
    private fun performCalculation() {
        val currentNumber = currentInput.toIntOrNull() ?: 0
        lastValue = when (lastOperator) {
            "+" -> lastValue + currentNumber
            "-" -> lastValue - currentNumber
            "x" -> lastValue * currentNumber
            "/" -> if (currentNumber != 0) lastValue / currentNumber else 0 // Manejar división entre 0
            else -> lastValue
        }
        currentInput = ""
        updateDisplay()
    }

    //Manejamos el boton de porcentaje %
    private fun onPercentage(view: View) {
        if (currentInput.isNotEmpty()) {
            // Pasaremos el numero actual a porcentaje
            val currentNumber = currentInput.toDoubleOrNull() ?: 0.0
            currentInput = (currentNumber / 100).toString()
            updateDisplay()
        }
    }

    //Metodo para las operaciones cientificas
    private fun onScientificFunction(view: View) {
        val button = view as Button
        val currentNumber = currentInput.toDoubleOrNull() ?: 0.0

        currentInput = when (button.id) {
            R.id.seno -> kotlin.math.sin(Math.toRadians(currentNumber)).toString()
            R.id.coseno -> kotlin.math.cos(Math.toRadians(currentNumber)).toString()
            R.id.tangente -> kotlin.math.tan(Math.toRadians(currentNumber)).toString()
            R.id.logaritmo -> if (currentNumber > 0) kotlin.math.log10(currentNumber).toString() else "Error"
            R.id.potencia -> {
                lastValue = currentInput.toInt()
                currentInput = "" // Espera un segundo número para la potencia
                "^"
            }
            else -> currentInput
        }

        updateDisplay()
    }



    //Metodo para actualizar el "texto" en el display
    private fun updateDisplay() {
        display.text = currentInput.ifEmpty { lastValue.toString() }
    }
}
