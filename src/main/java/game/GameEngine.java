package game;

import java.util.List;
import java.util.Scanner;

import model.Ship;
import model.Square;

public class GameEngine {

    private final GameController controller;
    private final ShipBuilder builder;

    private final String SHIP_HIT = "Ship %s hit!";
    private final String SHIP_SUNK = "Ship %s sunk!";

    public GameEngine(GameController controller, ShipBuilder builder) {
        this.controller = controller;
        this.builder = builder;
    }

    public void game() {
        List<Ship> ships = builder.getShips();
        List<Square> squaresInUse = builder.getSquaresInUse();
        Scanner scanner = new Scanner(System.in);
        Boolean allShipsSunk = false;
        System.out.println("Hello, it's a simple Ship Battle");
        System.out.println("You have to sink 3 ships: Battleship (5 squares), Destroyer1 (4 squares) and Destroyer2 (4 squares)");
        System.out.println();
        while (!allShipsSunk) {
            Square square = controller.getPlayerMove(scanner);
            if (square.getCoordinates().equals("X")) {
                break;
            }
            if (squaresInUse.contains(square)) {
                Ship ship = hitShip(square, ships);
                if (isShipSunk(ship)) {
                    System.out.println(String.format(SHIP_SUNK, ship.getName()));
                } else {
                    System.out.println(String.format(SHIP_HIT, ship.getName()));
                }
                square.setHit(true);
                if (allShipsSunk(squaresInUse).equals(true)) {
                    allShipsSunk = true;
                    System.out.println("All ships sunk!");
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

    private Ship hitShip(Square square, List<Ship> ships) {
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

    private Boolean allShipsSunk(List<Square> squaresInUse) {
        return squaresInUse.stream().filter(s -> s.getHit().equals(false)).count() == 0;
    }
}
