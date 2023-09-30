public interface IPokemonTrainer {

    void receiveCommand(String command);
    String getName();

    PokemonTeam getPokemonTeam();

}
