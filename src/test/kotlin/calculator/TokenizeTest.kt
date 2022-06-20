package calculator

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

class TokenizeTest {

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   ", "\t", "\n", "\t\n", "\n\t", " \n \t"])
    fun `공백 문자를 입력하면 빈 리스트를 출력한다`(input: String) {
        Assertions.assertThat(input.tokenize()).isEmpty()
    }

    @ParameterizedTest
    @MethodSource("provideTokenWhiteSpaceTestArgument")
    fun `앞선 공백 문자나 연속된 공백 문자, 뒤의 공백문자는 제거된다`(input: String, result: List<String>) {
        Assertions.assertThat(input.tokenize()).isEqualTo(result)
    }

    companion object {

        @JvmStatic
        fun provideTokenWhiteSpaceTestArgument(): Stream<Arguments> = Stream.of(
            Arguments.of("  1 + 2", listOf("1", "+", "2")),
            Arguments.of("13 - 21   ", listOf("13", "-", "21")),
            Arguments.of("15543 * 21121 \n ", listOf("15543", "*", "21121")),
            Arguments.of("-231 \t  /  232", listOf("-231", "/", "232")),
        )
    }
}
