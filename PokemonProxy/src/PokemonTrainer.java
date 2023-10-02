import java.io.Serial;
import java.io.Serializable;

public class PokemonTrainer implements IPokemonTrainer, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final PokemonTeam pokemonTeam;
    public PokemonTrainer(String name, PokemonTeam pokemonTeam) {
        this.name = name;
        this.pokemonTeam = pokemonTeam;
    }

    @Override
    public void receiveCommand(String command) {
        System.out.println("Trainer " + this.name + " is requesting command " + command);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public PokemonTeam getPokemonTeam() {
        return this.pokemonTeam;
    }

}
