package game;

import java.util.Scanner;
import java.util.regex.Pattern;

import model.Square;

public class GameController {

    private final Pattern pattern;

    public GameController() {
        this.pattern = Pattern.compile("^[A-Ja-j]([1-9]|10)$|^[Xx]$");
    }

    public Square getPlayerMove(Scanner scanner) {
        String coordinates = "";
        while (!pattern.matcher(coordinates).matches()) {
            System.out.println("Input correct coordinates (A1 - J10 or X for exit)");
            coordinates = scanner.nextLine();
        }
        coordinates = coordinates.toUpperCase();
        return Square.builder()
            .coordinates(coordinates)
            .hit(false)
            .build();
    }
}
