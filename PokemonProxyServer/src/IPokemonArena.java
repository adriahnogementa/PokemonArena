public interface IPokemonArena {
    void sendCommand(String command, IPokomonTrainer pokomonTrainer);
    void addPokemonTrainer(IPokomonTrainer pokomonTrainer);
    void exitPokemonTrainer(IPokomonTrainer pokomonTrainer);
}
