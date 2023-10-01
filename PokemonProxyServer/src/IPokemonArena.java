public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokomonTrainer);
    void sendPokemonTrainer(IPokemonTrainer pokomonTrainer);
    void enterPokemonArena(IPokemonTrainer pokomonTrainer);
    void exitPokemonArena(IPokemonTrainer pokomonTrainer);

    boolean arenaIsNotFull();
}
