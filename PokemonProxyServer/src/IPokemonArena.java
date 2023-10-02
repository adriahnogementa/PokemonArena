import java.io.IOException;

public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException;
    void addPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException;
    void removePokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException;

}
