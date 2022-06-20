package calculator

sealed interface Expression {

    fun eval(): Int

    object Undefined : Expression {

        override fun eval(): Int {
            throw IllegalArgumentException("Incomplete expression")
        }
    }

    data class Operator(
        val function: (Int, Int) -> Int,
        var firstOperand: Expression = Undefined,
        var secondOperand: Expression = Undefined
    ) : Expression {

        override fun eval(): Int {
            return function(firstOperand.eval(), secondOperand.eval())
        }
    }

    data class Operand(private val value: Int) : Expression {
        override fun eval(): Int = value
    }


    companion object {

        fun parse(input: String): Expression {
            return when (input) {
                "+" -> Operator(Int::plus)
                "-" -> Operator(Int::minus)
                "*" -> Operator(Int::times)
                "/" -> Operator(Int::div)
                else -> input.toIntOrNull()?.let { Operand(it) } ?: throw IllegalArgumentException()
            }
        }
    }
}
