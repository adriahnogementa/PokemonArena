import java.util.ArrayList;

public class PokemonTeam extends ArrayList<Pokemon> {


    public PokemonTeam() {
        super();
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
