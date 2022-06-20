package calculator

fun String.tokenize(): List<String> = this.trim().split("\\s+".toRegex()).filter { it != "" }

class Calculator(private val parser: Parser = NonPrecedenceParser()) {
    fun calculate(input: String?): Int {
        input ?: throw IllegalArgumentException("Null cannot be tokenized")
        return parser.parse(input.tokenize()).eval()
    }
}
