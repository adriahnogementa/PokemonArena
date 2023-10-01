import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonArena implements IPokemonArena {

    private final List<IPokemonTrainer> pokemonTrainers = new ArrayList<>();

    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException {
        if (!this.pokemonTrainers.contains(pokemonTrainer)){
            throw new RuntimeException("PokemonTrainer is not in the arena");
        }
        for (IPokemonTrainer trainer : this.pokemonTrainers){
                pokemonTrainer.receiveCommand( trainer.getName() + " said: " + command);
        }
    }
    @Override
    public void enterPokemonArena(IPokemonTrainer pokemonTrainer) throws IOException {
        this.pokemonTrainers.add(pokemonTrainer);
        sendCommand(pokemonTrainer.getName() + " entered the arena", pokemonTrainer);

    }

    @Override
    public void exitPokemonArena(IPokemonTrainer pokemonTrainer) throws IOException {
        this.pokemonTrainers.remove(pokemonTrainer);
        sendCommand(pokemonTrainer.getName() + " left the arena", pokemonTrainer);

    }

public List<IPokemonTrainer> getPokemonTrainers() {
        return pokemonTrainers;
    }


}
