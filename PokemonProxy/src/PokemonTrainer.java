import java.io.IOException;
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




    public PokemonTeam getPokemonTeam() {
        return this.pokemonTeam;
    }

    @Override
    public void receiveCommand(String command) throws IOException {
        System.out.println(this.name + " received: " + command);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
