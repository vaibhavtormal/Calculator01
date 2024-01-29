package com.vaibhav.calculator01

import android.health.connect.datatypes.units.Percentage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.vaibhav.calculator01.MainActivity.Operator.*
import com.vaibhav.calculator01.databinding.ActivityMainBinding
import org.w3c.dom.Text
import kotlin.text.StringBuilder

class MainActivity : AppCompatActivity() {
    private var inputValue1: Double? = 0.0
    private var inputValue2: Double? = null
    private var currentOperator: Operator? = null
    private var result: Double? = null
    private val equation: StringBuilder = StringBuilder().append(ZERO)


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setNightModeIndicator()
    }
    private fun setListeners(){
        for (button in getNumbericButtons()){
            button.setOnClickListener{ onNumberClicked(button.text.toString())}
        }
        with(binding){
            btnzero.setOnClickListener{onZeroClicked()}
            btnDoubleZero.setOnClickListener{onDoubleZeroCicked()}
            btnDecimalPoint.setOnClickListener{onDecimalPointClicked()}
            btnAddition.setOnClickListener{onOperatorCliked(Operator.ADDITION)}
            btnSubtraction.setOnClickListener{onOperatorCliked(Operator.SUBTRACTION)}
            btnMultiplication.setOnClickListener{onOperatorCliked(Operator.MULTIPLICATION)}
            btnDividion.setOnClickListener{onOperatorCliked(Operator.DIVIDER)}
            btnEqual.setOnClickListener{onEqualsClicked()}
            buttonAllClear.setOnClickListener{onAllClearClicked()}
            btnPlusMinus.setOnClickListener{onPlusMinusClicked()}
            btnPercentage.setOnClickListener{onPercentageClicked()}
            imageNightMode.setOnClickListener{toggleNightMode()}
        }
    }
    private fun toggleNightMode(){
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
    }
    private fun setNightModeIndicator() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.imageNightMode.setImageResource(R.drawable.contrast)
        } else {
            binding.imageNightMode.setImageResource(R.drawable.moon)
        }
    }
    private fun onPercentageClicked(){
        if (inputValue2 == null ){
            val percentage = getInputValue1() /100
            inputValue1 = percentage
            equation.clear().append(percentage)
            updateInputOnDisplay()
        }
        else{
            val percentageOfValue1 = (getInputValue1() * getInputValue2())/100
            val percentageOfValue2 = getInputValue2() / 100
            result = when(requireNotNull(currentOperator)){
                Operator.ADDITION -> getInputValue1() + percentageOfValue1
                Operator.SUBTRACTION ->getInputValue1() - percentageOfValue1
                Operator.MULTIPLICATION ->getInputValue1() * percentageOfValue2
                Operator.DIVIDER -> getInputValue1() /percentageOfValue2
            }
            equation.clear().append(ZERO)
            updateResultOnDisplay(isPercentage = true)
            inputValue1 = result
            result= null
            inputValue2 = null
            currentOperator = null
        }
    }
    private fun onPlusMinusClicked(){
        if (equation.startsWith(MINUS)){
            equation.deleteCharAt(0)
        }else{
         equation.insert(0, MINUS)

        }
        setInput()
        updateInputOnDisplay()
    }

    private fun onAllClearClicked(){
        inputValue1 = 0.0
        inputValue2 = null
        currentOperator = null
        result = null
        equation.clear().append(ZERO)
        clearDisplay()
    }

    private fun onOperatorCliked(operator : Operator){
        onEqualsClicked()
        currentOperator = operator
    }
    private fun onEqualsClicked(){
        if (inputValue2 != null){
            result = calculate()
            equation.clear().append(ZERO)
            updateResultOnDisplay()
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        }else{
            equation.clear().append(ZERO)
        }
    }
    private fun calculate() : Double{
        return when(requireNotNull(currentOperator)){
            Operator.ADDITION -> getInputValue1() + getInputValue2()
            Operator.SUBTRACTION-> getInputValue1() - getInputValue2()
            Operator.MULTIPLICATION -> getInputValue1() * getInputValue2()
            Operator.DIVIDER -> getInputValue1()/ getInputValue2()
        }
    }

    private fun onDecimalPointClicked(){
        if (equation.contains(DECIMAL_POINT))return
        equation.append(DECIMAL_POINT)
        setInput()
        updateInputOnDisplay()
    }
    private fun onZeroClicked(){
        if (equation.startsWith(ZERO))return
        onNumberClicked(ZERO)
    }
    private fun onDoubleZeroCicked(){
        if (equation.startsWith(ZERO))return
        onNumberClicked(Double_ZERO)
    }

    private fun getNumbericButtons() = with(binding){
        arrayOf(
            btnone,
            btntwo,
            btnthree,
            btnfour,
            btnfive,
            btnsix,
            btnseven,
            btneight,
            btnNine
        )
    }

    private  fun onNumberClicked(numberText:  String){
        if (equation.startsWith(ZERO)){
            equation.deleteCharAt(0)
        }else if (equation.startsWith("$MINUS$ZERO")){
            equation.deleteCharAt(1)
        }
        equation.append(numberText)
        setInput()
        updateInputOnDisplay()
    }
    private fun setInput(){
        if (currentOperator == null){
            inputValue1 = equation.toString().toDouble()
        }else{
            inputValue2 = equation.toString().toDouble()
        }
    }
    private fun  clearDisplay(){
        with(binding){
            textInput.text = getFromatterDisplayValue(value = getInputValue1())
            textEquation.text = null
        }
    }
    private fun updateResultOnDisplay(isPercentage:Boolean = false){
        binding.textInput.text = getFromatterDisplayValue(value = result)
        var input2Text = getFromatterDisplayValue(value = getInputValue2())
        if (isPercentage) input2Text = "$input2Text${getString(R.string.percentage)}"
        binding.textEquation.text = String.format(
            "%s %s %s",
            getFromatterDisplayValue(value = getInputValue1()),
            getOperatorSymbol(),
            input2Text
        )
    }

    private fun updateInputOnDisplay(){
        if (result ==null){
            binding.textEquation.text=null
        }
        binding.textInput.text = equation
    }
    private fun getInputValue1() = inputValue1 ?:0.0
    private fun getInputValue2() = inputValue2 ?:0.0

    private fun getOperatorSymbol(): String{
        return when(requireNotNull(currentOperator)){
            ADDITION -> getString(R.string.addition)
            SUBTRACTION -> getString(R.string.subtraction)
            MULTIPLICATION -> getString(R.string.multiplication)
            DIVIDER -> getString(R.string.division)

        }
    }

    private fun getFromatterDisplayValue(value:Double?):String?{
        val originalValue = value ?: return null
        return if (originalValue % 1==0.0){
            originalValue.toInt().toString()
        }else{
            originalValue.toString()
        }
    }

    enum class Operator{
        ADDITION,SUBTRACTION,MULTIPLICATION,DIVIDER

    }
    private companion object{
        const val DECIMAL_POINT = "."
        const val ZERO = "0"
        const val Double_ZERO = "00"
        const val MINUS = "-"


    }
}