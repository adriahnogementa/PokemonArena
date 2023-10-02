import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class PokemonTeam extends ArrayList<Pokemon> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public PokemonTeam() {
        super();
        this.add(new Pokemon("Pikachu", 100, 100, 100, 100));
        this.add(new Pokemon("Charmander", 100, 100, 100, 100));
        this.add(new Pokemon("Squirtle", 100, 100, 100, 100));
        this.add(new Pokemon("Bulbasaur", 100, 100, 100, 100));
        this.add(new Pokemon("Eevee", 100, 100, 100, 100));
        this.add(new Pokemon("Mew", 100, 100, 100, 100));
    }

    public void addPokemon(Pokemon pokemon) {
        if (this.size() >= 6) {
            System.out.println("You can't have more than 6 pokemon in your team!");
            return;
        }
        this.add(pokemon);
    }

    public void removePokemon(Pokemon pokemon) {
        if (this.size() <= 1) {
            System.out.println("You can't have less than 1 pokemon in your team!");
            return;
        }
        this.remove(pokemon);
    }
}
