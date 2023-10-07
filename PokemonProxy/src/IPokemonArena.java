import java.io.IOException;

public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException;
    boolean startBattle() throws IOException;
    void addPokemonTrainer(IPokemonTrainer pokemonTrainer);
    void removePokemonTrainer(IPokemonTrainer pokemonTrainer);

    String getEnemysPokemon(IPokemonTrainer pokemonTrainer) throws IOException;
    int getEnemysPokemonHealth(IPokemonTrainer pokemonTrainer) throws IOException;


}
