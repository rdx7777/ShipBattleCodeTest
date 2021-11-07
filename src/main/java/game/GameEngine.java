package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import model.Ship;
import model.Square;

public class GameEngine {

    private final GameController controller;
    private final List<Ship> ships = new ArrayList<>();
    private final List<Square> squaresInUse = new ArrayList<>();
    private final String[] columnNames = {"", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    private final String SHIP_HIT = "Ship %s hit!";
    private final String SHIP_SUNK = "Ship %s sunk!";

    public GameEngine(GameController controller) {
        this.controller = controller;
        createShips();
    }

    public void game() {
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
                Ship ship = hitShip(square);
                if (isShipSunk(ship)) {
                    System.out.println(String.format(SHIP_SUNK, ship.getName()));
                } else {
                    System.out.println(String.format(SHIP_HIT, ship.getName()));
                }
                square.setHit(true);
                if (allShipsSunk().equals(true)) {
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

    private Boolean allShipsSunk() {
        return squaresInUse.stream().filter(s -> s.getHit().equals(false)).count() == 0;
    }

    private void createShips() {
        createShipsWithoutSquares();
        List<Square> forbiddenSquares = new ArrayList<>(); // ship's squares, all squares around ships and all unsuccessful fields
        Random random = new Random();
        int column;
        int row;
        Square square;
        for (Ship ship : ships) {
            while (true) {
                column = random.nextInt(10) + 1;
                row = random.nextInt(10) + 1;
                square = createSquare(column, row);
                if (!forbiddenSquares.contains(square)) {
                    List<String> allowedDirections = isEnoughSpaceForShip(ship.getSquaresNumber(), column, row, forbiddenSquares);
                    if (!allowedDirections.isEmpty()) {
                        String direction = allowedDirections.get(random.nextInt(allowedDirections.size()));
                        System.out.println("Direction chosen: " + direction);
                        createShipSquares(ship, column, row, direction, forbiddenSquares);
                        protectShip(ship, forbiddenSquares);
                        break;
                    } else {
                        forbiddenSquares.add(square);
                    }
                }
            }
        }
        forbiddenSquares.clear();
    }

    private void createShipsWithoutSquares() {
        Ship ship0 = Ship.builder()
            .name("Battleship")
            .shipSquares(new ArrayList<>())
            .squaresNumber(5)
            .build();
        Ship ship1 = Ship.builder()
            .name("Destroyer1")
            .shipSquares(new ArrayList<>())
            .squaresNumber(4)
            .build();
        Ship ship2 = Ship.builder()
            .name("Destroyer2")
            .shipSquares(new ArrayList<>())
            .squaresNumber(4)
            .build();
        ships.addAll(Arrays.asList(ship0, ship1, ship2));
    }

    private Square createSquare(int column, int row) {
        return new Square(columnNames[column] + row, false);
    }

    private List<String> isEnoughSpaceForShip(int squaresNumber, int column, int row, List<Square> forbiddenSquares) {
        boolean checker;
        int i;
        List<String> allowedDirections = new ArrayList<>();
        Square square = new Square("", false);
        if (column - squaresNumber >= 0) {
            checker = true;
            i = column - 1;
            while (i > 0) {
                square.setCoordinates(columnNames[i] + row);
                if (forbiddenSquares.contains(square)) {
                    checker = false;
                    break;
                }
                i--;
            }
            if (checker) {
                allowedDirections.add("LEFT");
                System.out.println("added LEFT");
            }
        }
        if (row - squaresNumber >= 0) {
            checker = true;
            i = row - 1;
            while (i > 0) {
                square.setCoordinates(columnNames[column] + i);
                if (forbiddenSquares.contains(square)) {
                    checker = false;
                    break;
                }
                i--;
            }
            if (checker) {
                allowedDirections.add("UP");
                System.out.println("added UP");
            }
        }
        if (column + squaresNumber - 1 <= 10) {
            checker = true;
            i = column + 1;
            while (i <= 10) {
                square.setCoordinates(columnNames[i] + row);
                if (forbiddenSquares.contains(square)) {
                    checker = false;
                    break;
                }
                i++;
            }
            if (checker) {
                allowedDirections.add("RIGHT");
                System.out.println("added RIGHT");
            }
        }
        if (row + squaresNumber - 1 <= 10) {
            checker = true;
            i = row + 1;
            while (i <= 10) {
                square.setCoordinates(columnNames[column] + i);
                if (forbiddenSquares.contains(square)) {
                    checker = false;
                    break;
                }
                i++;
            }
            if (checker) {
                allowedDirections.add("DOWN");
                System.out.println("added DOWN");
            }
        }
        return allowedDirections;
    }

    private void createShipSquares(Ship ship, int column, int row, String direction, List<Square> forbiddenSquares) {
        List<Square> shipSquares = ship.getShipSquares();
        switch (direction) {
            case "LEFT":
                for (int i = 0; i < ship.getSquaresNumber(); i++) {
                    Square square = createSquare(column - i, row);
                    addSquareToLists(square, shipSquares, forbiddenSquares);
                }
                break;
            case "UP":
                for (int i = 0; i < ship.getSquaresNumber(); i++) {
                    Square square = createSquare(column, row - i);
                    addSquareToLists(square, shipSquares, forbiddenSquares);
                }
                break;
            case "RIGHT":
                for (int i = 0; i < ship.getSquaresNumber(); i++) {
                    Square square = createSquare(column + i, row);
                    addSquareToLists(square, shipSquares, forbiddenSquares);
                }
                break;
            case "DOWN":
                for (int i = 0; i < ship.getSquaresNumber(); i++) {
                    Square square = createSquare(column, row + i);
                    addSquareToLists(square, shipSquares, forbiddenSquares);
                }
                break;
        }
    }

    private void protectShip(Ship ship, List<Square> forbiddenSquares) {
        for (Square square : ship.getShipSquares()) {
            String columnName = String.valueOf(square.getCoordinates().charAt(0));
            int column = Arrays.asList(columnNames).indexOf(columnName);
            int row = Integer.valueOf(square.getCoordinates().substring(1));
            System.out.println("PROTECT: column " + column + " & row " + row );
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (column + i > 0 && column + i <= 10 && row + j > 0 && row + j <= 10) {
                        System.out.println("PROTECT: column " + (column + i) + " & row " + (row + j));
                        Square protectedSquare = createSquare(column + i, row + j);
                        if (!forbiddenSquares.contains(protectedSquare)) {
                            forbiddenSquares.add(protectedSquare);
                        }
                    }
                }
            }
        }
    }

    private void addSquareToLists(Square square, List<Square> shipSquares, List<Square> forbiddenSquares) {
        shipSquares.add(square);
        forbiddenSquares.add(square);
        squaresInUse.add(square);
    }
}
