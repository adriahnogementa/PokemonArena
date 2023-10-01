import java.util.ArrayList;
import java.util.List;

public class PokemonArena implements IPokemonArena {

    private List<IPokemonTrainer> pokemonTrainers = new ArrayList<>();

    @Override
    public void sendCommand(String command, IPokemonTrainer pokomonTrainer) {
        if (!this.pokemonTrainers.contains(pokomonTrainer)){
            throw new RuntimeException("PokemonTrainer is not in the arena");
        }
        for (IPokemonTrainer pokemonTrainer : this.pokemonTrainers){
                pokemonTrainer.receiveCommand(command);
        }
        
    }

    @Override
    public void sendPokemonTrainer(IPokemonTrainer pokomonTrainer) {

    }

    @Override
    public void enterPokemonArena(IPokemonTrainer pokomonTrainer) {

    }

    @Override
    public void exitPokemonArena(IPokemonTrainer pokomonTrainer) {

    }


    public boolean arenaIsNotFull(){
        return this.pokemonTrainers.size() < 2;}
}
