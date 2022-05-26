package calculator

fun String?.tokenize(): List<String> = this?.trim()?.split("\\s+".toRegex())?.filter { it != "" }
    ?: throw IllegalArgumentException("Null cannot be tokenized")

interface Parser {
    fun parse(tokenList: List<String>): Expression
}

class NonPrecedenceParser : Parser {

    override fun parse(tokenList: List<String>): Expression {
        tailrec fun parse(exp: Expression, tokenList: List<String>): Expression {
            if (tokenList.isEmpty()) return exp

            val newExp = when (val parsed = Expression.parse(tokenList.first())) {
                is Expression.Operand -> when (exp) {
                    Expression.Undefined -> parsed
                    is Expression.Operator -> exp.also { it.secondOperand = parsed }
                    else -> throw IllegalArgumentException("Invalid expression")
                }
                is Expression.Operator -> parsed.also { it.firstOperand = exp }
                else -> throw IllegalArgumentException("Invalid expression")
            }

            return parse(newExp, tokenList.drop(1))
        }

        return parse(Expression.Undefined, tokenList)
    }
}

class Calculator(private val parser: Parser = NonPrecedenceParser()) {

    fun calculate(input: String): Int = parser.parse(input.tokenize()).eval()
}

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
