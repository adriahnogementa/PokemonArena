import java.io.IOException;

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
        System.out.println(command);
    }

    @Override
    public void receiveCommand(String command) throws IOException {
        System.out.println(command);
    }

    @Override
    public boolean pokemonIsAlive() {
        return !this.pokemon.isDead();
    }

    @Override
    public void takeDamage(int damage) {
        this.pokemon.takeDamage(damage);
    }

    @Override
    public int getAttackDamage() {
        return this.pokemon.getAttack();
    }

    @Override
    public int getInitiative() {
        return this.pokemon.getInitiative();
    }

    @Override
    public int getDodgeChance() {
        return this.pokemon.getDodgeChance();
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
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
}


