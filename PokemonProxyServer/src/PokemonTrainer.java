import java.io.Serial;
import java.io.Serializable;

public record PokemonTrainer(String name, PokemonTeam pokemonTeam) implements IPokemonTrainer, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void receiveCommand(String command) {
        System.out.println("Trainer " + this.name + " is requesting command " + command);
    }

}
