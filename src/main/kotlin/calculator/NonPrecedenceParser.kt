package calculator

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
