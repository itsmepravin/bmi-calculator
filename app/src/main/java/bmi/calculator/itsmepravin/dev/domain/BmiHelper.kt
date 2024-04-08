package bmi.calculator.itsmepravin.dev.domain

object BmiHelper {

    fun calculateBmi(weightInKg: Int, heightInCm: Int): Double {
        val heightInMeter = heightInCm / 100.0
        return weightInKg / (heightInMeter * heightInMeter)
    }

}