import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonArena implements IPokemonArena {

    private final List<IPokemonTrainer> pokemonTrainers = new ArrayList<>();

    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException {
          if(!battleHasStarted()){
              pokemonTrainer.receiveMessage("Battle has not started yet!");
              return;
          }

            if (!pokemonTrainer.hasAction() && getTheOtherTrainer(pokemonTrainer).hasAction()) {
                pokemonTrainer.receiveMessage("You have no action! Wait for your turn!");
                return;
            }
        if (command.startsWith("1")) {
            pokemonTrainer.receiveMessage("You have chosen to attack!");
            pokemonTrainers.forEach((pt) -> {
                if (!pt.equals(pokemonTrainer)) {
                    try {
                        pt.receiveMessage(pokemonTrainer.getName() + " has chosen to attack!");
                        //TODO: pt.receiveCommand("1"); + Irgendwie Damage einbauen
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } else if (command.startsWith("2")) {
            pokemonTrainer.receiveMessage("You have chosen to defend!");
            pokemonTrainers.forEach((pt) -> {
                if (!pt.equals(pokemonTrainer)) {
                    try {
                        pt.receiveMessage(pokemonTrainer.getName() + " has chosen to defend!");
                        //TODO: pt.receiveCommand("2"); + Irgendwie Dodge einbauen
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        pokemonTrainer.setActionStatus(false);
        if (!onePokemonTrainerHasAction()){
            pokemonTrainers.forEach((pt) -> {
                try {
                    pt.setActionStatus(true);
                    pt.receiveMessage("You have action again!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }


    }

    private boolean onePokemonTrainerHasAction() throws IOException {
        return pokemonTrainers.get(0).hasAction() | pokemonTrainers.get(1).hasAction();
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
