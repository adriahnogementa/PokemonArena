import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class PokemonTrainer implements IPokemonTrainer {
    private boolean readyForBattle = false;
    private boolean hasAction = true;
    private final String name;
    private final Pokemon pokemon;
    public PokemonTrainer(String name, Pokemon pokemon) {
        this.name = name;
        this.pokemon = pokemon;
    }





    @Override
    public boolean readyForBattle() throws IOException {
        return this.readyForBattle;
    }

    @Override
    public void receiveMessage(String command) throws IOException {
        System.out.println(this.name + " received: " + command);
    }

    @Override
    public void receiveCommand(String command) throws IOException {
        System.out.println(this.name + " received: " + command);
    }

    @Override
    public String getName() {
        return this.name;
    }


    public void setReadyForBattle(boolean readyForBattle) {
        this.readyForBattle = readyForBattle;
    }

    public boolean isReadyForBattle() {
        return this.readyForBattle;
    }

    public boolean hasAction() {
        return this.hasAction;
    }

    @Override
    public void setActionStatus(boolean hasAction) {
        this.hasAction = hasAction;
        System.out.println("ActionStatus changed: " + hasAction);
    }


    public void setHasAction(boolean hasAction) {
        this.hasAction = hasAction;
    }
}


