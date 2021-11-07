package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

import model.Square;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @InjectMocks
    private GameController controller;

    private Scanner scanner;

    @ParameterizedTest
    @MethodSource("arguments")
    void shouldReturnPlayerMove(String input, Square square) throws IOException {
        // given
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in);

        // when
        Square result = controller.getPlayerMove(scanner);

        // then
        assertEquals(square, result);
    }

    static Stream<Arguments> arguments() {
        return Stream.of(
            Arguments.of("A10", new Square("A10", false)),
            Arguments.of("G4", new Square("G4", false)),
            Arguments.of("b9", new Square("B9", false)),
            Arguments.of("c3", new Square("C3", false)),
            Arguments.of("X", new Square("X", false)),
            Arguments.of("x", new Square("X", false))
        );
    }
}
