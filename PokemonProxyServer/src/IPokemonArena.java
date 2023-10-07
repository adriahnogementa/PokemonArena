import java.io.IOException;

public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException;
    boolean startBattle() throws IOException;
    void addPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException;
    void removePokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException;

    String getEnemysPokemon(IPokemonTrainer pokemonTrainer) throws IOException;
    int getEnemysPokemonHealth(IPokemonTrainer pokemonTrainer) throws IOException;

}
