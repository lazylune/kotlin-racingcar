package calculator

interface Parser {
    fun parse(tokenList: List<String>): Expression
}
