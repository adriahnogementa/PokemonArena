import java.util.ArrayList;

public class PokemonTeam extends ArrayList<Pokemon> {
    public PokemonTeam() {
        super();
    }

    public PokemonTeam(int initialCapacity) {
        super(initialCapacity);
    }

    public PokemonTeam(PokemonTeam pokemonTeam) {
        super(pokemonTeam);
    }
}
