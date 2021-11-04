import game.GameController;
import game.GameEngine;

public class ShipBattleApplication {

    public static void main(String[] args) {
        GameController controller = new GameController();
        GameEngine engine = new GameEngine(controller);
        engine.game();
    }
}
