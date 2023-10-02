import java.util.List;

public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokomonTrainer);
    void enterPokemonArena(IPokemonTrainer pokomonTrainer);
    void exitPokemonArena(IPokemonTrainer pokomonTrainer);


}
