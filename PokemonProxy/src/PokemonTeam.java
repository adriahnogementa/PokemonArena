import java.sql.SQLOutput;
import java.util.ArrayList;

public class PokemonTeam extends ArrayList<Pokemon> {


    public PokemonTeam() {
        super();
        this.add(new Pokemon("Pikachu", 150, 10, 5, 5));
        this.add(new Pokemon("Charmander", 200, 20, 9, 2));
        this.add(new Pokemon("Squirtle", 100, 15, 7, 2));
        this.add(new Pokemon("Bulbasaur", 130, 10, 4, 3));
        this.add(new Pokemon("Mewtwo", 300, 30, 10, 1));
    }


    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PokemonTeam: \n");
        for (int i = 0; i < this.size(); i++) {
            stringBuilder.append(i + 1).append(". ").append(this.get(i).getName()).append(" ");
            stringBuilder.append("HP: ").append(this.get(i).getHp()).append(" ");
            stringBuilder.append("Attack: ").append(this.get(i).getAttack()).append(" ");
            stringBuilder.append("Initiative: ").append(this.get(i).getInitiative()).append(" ");
            stringBuilder.append("Dodge Chance: ").append(this.get(i).getDodgeChance()).append(" ");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Pokemon getPokemon(int i) {
        return this.get(i-1);
    }

    public Pokemon findPokemonByName(String name) {
        for (Pokemon pokemon : this) {
            if (pokemon.getName().equals(name)) {
                return pokemon;
            }
        }
        return null;
    }
}
