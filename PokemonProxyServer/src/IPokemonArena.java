import java.io.IOException;
import java.util.List;

public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException;
    void enterPokemonArena(IPokemonTrainer pokemonTrainer) throws IOException;
    void exitPokemonArena(IPokemonTrainer pokemonTrainer) throws IOException;

    List<IPokemonTrainer> getPokemonTrainers();
}
