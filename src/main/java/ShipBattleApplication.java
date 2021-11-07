import game.GameController;
import game.GameEngine;
import game.ShipBuilder;

public class ShipBattleApplication {

    public static void main(String[] args) {
        GameController controller = new GameController();
        ShipBuilder builder = new ShipBuilder();
        GameEngine engine = new GameEngine(controller, builder);
        engine.game();
    }
}
