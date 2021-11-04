package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Ship;
import model.Square;

public class GameEngine {

    private final GameController controller;
    private final List<Ship> ships = new ArrayList<>();
    private final List<Square> squaresInUse = new ArrayList<>();

    private final String SHIP_HIT = "Ship %s hit!";
    private final String SHIP_SUNK = "Ship %s sunk!";

    public GameEngine(GameController controller) {
        this.controller = controller;
        createShips();
        createSquaresInUseList();
    }

    public void game() {
        Scanner scanner = new Scanner(System.in);
        Boolean allShipsSunk = false;
        while (!allShipsSunk) {
            Square square = controller.getPlayerMove(scanner);
            if (square.getCoordinates().equals("X")) {
                break;
            }
            if (squaresInUse.contains(square)) {
                Ship ship = hitShip(square);
                if (isShipSunk(ship)) {
                    System.out.println(String.format(SHIP_SUNK, ship.getName()));
                } else {
                    System.out.println(String.format(SHIP_HIT, ship.getName()));
                }
                square.setHit(true);
                if (allShipsSunk(square).equals(true)) {
                    allShipsSunk = true;
                }
            } else {
                System.out.println("Missed. Try again.");
                scanner.reset();
            }
        }
        if (allShipsSunk.equals(true)) {
            System.out.println("You won!");
        } else {
            System.out.println("Game finished by player.");
        }
        scanner.close();
        ships.clear();
        squaresInUse.clear();
    }

    private Ship hitShip(Square square) {
        for (Ship ship : ships) {
            if (ship.getShipSquares().contains(square)) {
                int i = ship.getShipSquares().indexOf(square);
                ship.getShipSquares().get(i).setHit(true);
                return ship;
            }
        }
        return null;
    }

    private Boolean isShipSunk(Ship ship) {
        return ship.getShipSquares().stream().filter(s -> s.getHit().equals(false)).count() == 0;
    }

    private Boolean allShipsSunk(Square square) {
        int i = squaresInUse.indexOf(square);
        squaresInUse.get(i).setHit(true);
        return squaresInUse.stream().filter(s -> s.getHit().equals(false)).count() == 0;
    }

    private void createShips() {
        // TODO: add real logic
        Ship ship0 = Ship.builder()
            .name("Battleship")
            .shipSquares(Arrays.asList(new Square("A1", false),
                new Square("A2", false),
                new Square("A3", false),
                new Square("A4", false),
                new Square("A5", false)))
            .build();
        Ship ship1 = Ship.builder()
            .name("Destroyer1")
            .shipSquares(Arrays.asList(new Square("C1", false),
                new Square("C2", false),
                new Square("C3", false),
                new Square("C4", false)))
            .build();
        Ship ship2 = Ship.builder()
            .name("Destroyer2")
            .shipSquares(Arrays.asList(new Square("J1", false),
                new Square("J2", false),
                new Square("J3", false),
                new Square("J4", false)))
            .build();
        ships.addAll(Arrays.asList(ship0, ship1, ship2));
    }

    private void createSquaresInUseList() {
        for (Ship ship : ships) {
            squaresInUse.addAll(ship.getShipSquares());
        }
    }
}
