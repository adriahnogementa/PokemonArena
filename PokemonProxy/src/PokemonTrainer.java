public class PokemonTrainer implements IPokemonTrainer {

    private final String name;
    private PokemonTeam pokemonTeam;
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

    @Override
    public PokemonTeam getPokemonTeam() {
        return this.pokemonTeam;
    }


}

// Path: PokemonProxyServer/src/PokemonArena.java
