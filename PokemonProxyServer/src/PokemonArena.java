import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PokemonArena implements IPokemonArena {

    private final List<IPokemonTrainer> pokemonTrainers = new ArrayList<>();
    private final Hashtable<IPokemonTrainer, String> pokemonTrainerBattleRound = new Hashtable<>();

    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException {
          if(!battleHasStarted()){
              pokemonTrainer.receiveMessage("Battle has not started yet!");
              return;
          }

            if (!pokemonTrainer.hasAction() ) {
                pokemonTrainer.receiveMessage("You have no action! Wait for your turn!");
                return;
            }

            if (command.startsWith("1")){
                pokemonTrainerBattleRound.put(pokemonTrainer, "1");
                for (IPokemonTrainer pt : pokemonTrainers){
                    if (!pt.equals(pokemonTrainer)){
                        pt.receiveMessage(pokemonTrainer.getName() + " used " + "Attack!");
                    }
                }
            } else if (command.startsWith("2")) {
                for (IPokemonTrainer pt : pokemonTrainers){
                    if (!pt.equals(pokemonTrainer)){
                        pt.receiveMessage(pokemonTrainer.getName() + " used " + "Dodge!");
                    }
                }
            }
            pokemonTrainer.setActionStatus(false);

    }


    private boolean battleHasStarted() throws IOException {
        return pokemonTrainers.get(0).readyForBattle() && pokemonTrainers.get(1).readyForBattle();
    }

    private IPokemonTrainer getTheOtherTrainer(IPokemonTrainer pokemonTrainer){
        for (IPokemonTrainer pt : pokemonTrainers){
            if (!pt.equals(pokemonTrainer)){
                return pt;
            }
        }
        return null;
    }


    @Override
    public boolean startBattle() throws IOException {
        if (this.pokemonTrainers.size() < 2) {
            return false;
        }

        for (IPokemonTrainer pokemonTrainer : this.pokemonTrainers) {
            if (!pokemonTrainer.readyForBattle()) {
                pokemonTrainer.receiveMessage("You are not ready for battle! Join the fight, coward!!");
                pokemonTrainers.forEach((pt) -> {
                    if (!pt.equals(pokemonTrainer)) {
                        try {
                            pt.receiveMessage(pokemonTrainer.getName() + " is not ready for battle! Wait for him!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                return true;
            }
        }
        for (IPokemonTrainer pt : this.pokemonTrainers) {
            pt.receiveMessage("All Trainers are ready for battle! Let the battle begin!");
        }
        return true;
    }

    @Override
    public void addPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException {
        this.pokemonTrainers.add(pokemonTrainer);

    }

    @Override
    public void removePokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException {
        this.pokemonTrainers.remove(pokemonTrainer);

    }

}
