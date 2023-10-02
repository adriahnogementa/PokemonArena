public interface IPokemonArena {
    void sendCommand(String command, IPokemonTrainer pokemonTrainer);
    void addPokemonTrainer(IPokemonTrainer pokemonTrainer);
    void removePokemonTrainer(IPokemonTrainer pokemonTrainer);


}
