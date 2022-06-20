package calculator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class CalculatorTest {

    private val calculator = Calculator()

    @Test
    fun `null 이 입력되면 IllegalArgumentException 을 던진다`() {
        assertThatThrownBy {
            calculator.calculate(null)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @CsvSource(value = ["1 + 2:3", "2 + 3:5", "10 + 0:10", "100 + 100:200", "-1 + 2:1", "1 + -2:-1"], delimiter = ':')
    fun `덧셈 연산`(input: String, result: Int) {
        assertThat(calculator.calculate(input)).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(value = ["1 - 2:-1", "5 - 3:2", "0 - 9:-9", "10 - 0:10"], delimiter = ':')
    fun `뺄셈 연산`(input: String, result: Int) {
        assertThat(calculator.calculate(input)).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(value = ["1 * 1:1", "5 * 0:0", "0 * 9:0", "10 * 5:50", "10 * -1:-10"], delimiter = ':')
    fun `곱셈 연산`(input: String, result: Int) {
        assertThat(calculator.calculate(input)).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(value = ["4 / 2:2", "5 / 2:2", "100 / 101:0", "100 / -10:-10"], delimiter = ':')
    fun `나눗셈 연산`(input: String, result: Int) {
        assertThat(calculator.calculate(input)).isEqualTo(result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["5 / 0", "3 + 4 / 0", "10 / 0 * 4"])
    fun `0 으로 나누는 경우 ArithmeticException 을 던진다`(input: String) {
        assertThatThrownBy { calculator.calculate(input) }.isInstanceOf(ArithmeticException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = ["4 ^ 3", "abc + 가나다", "32.21 + 0.94", "@2$3"])
    fun `사칙 연산 기호가 아닌 경우에는 IllegalArgumentException 을 던진다`(input: String) {
        assertThatThrownBy { calculator.calculate(input) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `입력 값에 따라 계산 순서가 결정된다`() {
        assertThat(calculator.calculate("1 + 2 * 3")).isEqualTo(9)
        assertThat(calculator.calculate("4 - 2 / 2")).isEqualTo(1)
        assertThat(calculator.calculate("7 + 3 * 6 - 22 / 2")).isEqualTo(19)
    }
}
