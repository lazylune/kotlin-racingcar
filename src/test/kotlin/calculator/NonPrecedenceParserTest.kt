package calculator

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class NonPrecedenceParserTest {

    private val parser = NonPrecedenceParser()

    @Test
    fun `빈 리스트는 Undefined object 를 리턴한다`() {
        Assertions.assertThat(parser.parse(emptyList())).isEqualTo(Expression.Undefined)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, -1, 100, Int.MAX_VALUE, Int.MIN_VALUE])
    fun `단일 숫자 리스트는 숫자 값을 value 로 갖는 Operand 클래스를 리턴한다`(input: Int) {
        Assertions.assertThat(parser.parse(listOf(input.toString()))).isEqualTo(Expression.Operand(input))
    }

    @Test
    fun `사칙 연산자는 Operator 클래스를 리턴한다`() {
        Assertions.assertThat(parser.parse(listOf("+"))).isEqualTo(Expression.Operator(Int::plus))
        Assertions.assertThat(parser.parse(listOf("-"))).isEqualTo(Expression.Operator(Int::minus))
        Assertions.assertThat(parser.parse(listOf("*"))).isEqualTo(Expression.Operator(Int::times))
        Assertions.assertThat(parser.parse(listOf("/"))).isEqualTo(Expression.Operator(Int::div))
    }

    @Test
    fun `사칙 연산은 피 연산자를 operand 로 갖는 Operator 클래스를 리턴한다`() {
        Assertions.assertThat(parser.parse(listOf("1", "+", "-1"))).isEqualTo(
            Expression.Operator(Int::plus, Expression.Operand(1), Expression.Operand(-1))
        )
        Assertions.assertThat(parser.parse(listOf("24", "-", "31"))).isEqualTo(
            Expression.Operator(Int::minus, Expression.Operand(24), Expression.Operand(31))
        )
        Assertions.assertThat(parser.parse(listOf("6", "*", "53"))).isEqualTo(
            Expression.Operator(Int::times, Expression.Operand(6), Expression.Operand(53))
        )
        Assertions.assertThat(parser.parse(listOf("7", "/", "2"))).isEqualTo(
            Expression.Operator(Int::div, Expression.Operand(7), Expression.Operand(2))
        )
    }

    @Test
    fun `숫자와 사칙연산 이외의 문자열은 IllegalArgumentException 을 던진다`() {
        Assertions.assertThatThrownBy { parser.parse(listOf("31.42", "+", "-1")) }.isInstanceOf(IllegalArgumentException::class.java)
        Assertions.assertThatThrownBy { parser.parse(listOf("42", "^", "21")) }.isInstanceOf(IllegalArgumentException::class.java)
        Assertions.assertThatThrownBy { parser.parse(listOf("(", "3", ")")) }.isInstanceOf(IllegalArgumentException::class.java)
        Assertions.assertThatThrownBy { parser.parse(listOf("abc", "+", "가나다")) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `여러 사칙 연산이 조합된 수식은 마지막 연산자의 Operator 클래스를 리턴한다`() {
        val parsed = parser.parse(listOf("7", "+", "2", "-", "3"))
        Assertions.assertThat(parsed).isInstanceOf(Expression.Operator::class.java)

        val operator = parsed as Expression.Operator
        Assertions.assertThat(operator.function).isEqualTo((parser.parse(listOf("-")) as Expression.Operator).function)
        Assertions.assertThat(operator.firstOperand).isEqualTo(parser.parse(listOf("7", "+", "2")))
        Assertions.assertThat(operator.secondOperand).isEqualTo(Expression.Operand(3))
    }
}
