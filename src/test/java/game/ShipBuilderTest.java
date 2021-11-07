package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import model.Ship;
import model.Square;
import org.junit.jupiter.api.Test;

class ShipBuilderTest {

    @Test
    void shouldBuildShips() {
        // when
        ShipBuilder shipBuilder = new ShipBuilder();

        // then
        assertFalse(shipBuilder.getShips().isEmpty());
        assertEquals(3, shipBuilder.getShips().size());
        assertEquals(13, shipBuilder.getSquaresInUse().size());
    }

    @Test
    void shouldReturnCorrectListOfShipsAndSquaresInUse() {
        // given
        ShipBuilder shipBuilder = new ShipBuilder();

        // when
        List<Ship> ships = shipBuilder.getShips();
        List<Square> shipsSquares = new ArrayList<>();
        for (Ship ship : ships) {
            shipsSquares.addAll(ship.getShipSquares());
        }
        List<Square> squaresInUse = shipBuilder.getSquaresInUse();

        // then
        for (Square square : shipsSquares) {
            assertTrue(squaresInUse.contains(square));
        }
    }
}
