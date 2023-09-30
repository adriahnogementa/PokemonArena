public interface IPokemonArena {
    void sendCommand(String command, IPokomonTrainer pokomonTrainer);
    void enterPokemonArena(IPokomonTrainer pokomonTrainer);
    void exitPokemonArena(IPokomonTrainer pokomonTrainer);
}
