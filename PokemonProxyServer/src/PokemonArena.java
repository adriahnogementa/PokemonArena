import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PokemonArena implements IPokemonArena {

    private final List<IPokemonTrainer> pokemonTrainers = new ArrayList<>();
    private final Hashtable<IPokemonTrainer, String> pokemonTrainerBattleRound = new Hashtable<>();
    private boolean gameOver = false;


    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException {
        if (gameOver) {
            gameOverBroadcast();
            return;
        }

        if (!battleHasStarted()) {
            pokemonTrainer.receiveMessage("Battle has not started yet!");
            return;
        }

        if (!pokemonTrainer.hasAction()) {
            pokemonTrainer.receiveMessage("You have no action! Wait for your turn!");
            return;
        }

        if (command.startsWith("1")) {
            pokemonTrainerBattleRound.put(pokemonTrainer, "1");
            for (IPokemonTrainer pt : pokemonTrainers) {
                if (!pt.equals(pokemonTrainer)) {
                    pt.receiveMessage(pokemonTrainer.getName() + " set his action!");
                }
            }
        } else if (command.startsWith("2")) {
            pokemonTrainerBattleRound.put(pokemonTrainer, "2");
            for (IPokemonTrainer pt : pokemonTrainers) {
                if (!pt.equals(pokemonTrainer)) {
                    pt.receiveMessage(pokemonTrainer.getName() + " set his action!");
                }
            }
        }
        pokemonTrainer.setActionStatus(false);

        if (battleRoundReady()) {
            resolveBattleRound();

        }
    }

    //Here is the logic of the battle
    //If both PokemonTrainers choose to attack, the PokemonTrainer with the higher initiative attacks first
    //If both PokemonTrainers choose to dodge, nothing happens
    //If one PokemonTrainer chooses to attack and the other to dodge, the PokemonTrainer who chose to attack attacks first
    //If both PokemonTrainers have the same initiative, the random PokemonTrainer attacks first
    //After each Attack there is a check if the pokemon is still alive
    //If the pokemon is dead, the game is over
    private void resolveBattleRound() throws IOException {
        IPokemonTrainer pokemonTrainer1 = pokemonTrainers.get(0);
        IPokemonTrainer pokemonTrainer2 = pokemonTrainers.get(1);
        if (pokemonTrainerBattleRound.get(pokemonTrainer1).equals("1") && pokemonTrainerBattleRound.get(pokemonTrainer2).equals("1")) { // Attack & Attack
            int pokemonTrainer1Initiative = pokemonTrainer1.getInitiative();
            int pokemonTrainer2Initiative = pokemonTrainer2.getInitiative();
            int pokemonTrainer1AttackDamage = pokemonTrainer1.getAttackDamage();
            int pokemonTrainer2AttackDamage = pokemonTrainer2.getAttackDamage();
            if (pokemonTrainer1Initiative > pokemonTrainer2Initiative) {
                broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " took damage: " + pokemonTrainer1AttackDamage + " !");
                pokemonTrainer2.takeDamage(pokemonTrainer1AttackDamage);
                if (!pokemonTrainer2.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " is dead!");
                    gameOver = true;
                }
                broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " took damage: " + pokemonTrainer2AttackDamage + " !");
                pokemonTrainer1.takeDamage(pokemonTrainer2AttackDamage);
                if (!pokemonTrainer1.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " is dead!");
                    gameOver = true;
                }
            } else if (pokemonTrainer1Initiative < pokemonTrainer2Initiative) {
                broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " took damage: " + pokemonTrainer2AttackDamage + " !");
                pokemonTrainer1.takeDamage(pokemonTrainer2AttackDamage);
                if (!pokemonTrainer1.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " is dead!");
                    gameOver = true;
                }
                broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " took damage: " + pokemonTrainer1AttackDamage + " !");
                pokemonTrainer2.takeDamage(pokemonTrainer1AttackDamage);
                if (!pokemonTrainer2.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " is dead!");
                    gameOver = true;
                }
            } else {
                broadcastMessage("Both Pokemons have the same initiative! Let the Bones decide!");
                IPokemonTrainer pokemonTrainerWinner = randomPokemonTrainer();
                IPokemonTrainer pokemonTrainerLooser = getOtherPokemonTrainer(pokemonTrainerWinner);

                broadcastMessage(pokemonTrainerLooser.getName() + "`s " + pokemonTrainerLooser.getPokemonName() + " took damage: " + pokemonTrainerWinner.getAttackDamage() + " !");
                pokemonTrainerLooser.takeDamage(pokemonTrainerWinner.getAttackDamage());
                if (!pokemonTrainerLooser.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainerLooser.getName() + "`s " + pokemonTrainerLooser.getPokemonName() + " is dead!");
                    gameOver = true;
                }else {
                    broadcastMessage(pokemonTrainerWinner.getName() + "`s " + pokemonTrainerWinner.getPokemonName() + " took damage: " + pokemonTrainerLooser.getAttackDamage() + " !");
                    pokemonTrainerWinner.takeDamage(pokemonTrainerLooser.getAttackDamage());
                    if (!pokemonTrainerWinner.pokemonIsAlive()) {
                        broadcastMessage(pokemonTrainerWinner.getName() + "`s " + pokemonTrainerWinner.getPokemonName() + " is dead!");
                        gameOver = true;
                    }
                }

            }

        } else if (pokemonTrainerBattleRound.get(pokemonTrainer1).equals("1") && pokemonTrainerBattleRound.get(pokemonTrainer2).equals("2")) {// Attack & Dodge
            if (randomBoolean(pokemonTrainer2.getDodgeChance())) {
                broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " dodged!");
            } else {
                int damage = pokemonTrainer1.getAttackDamage();
                broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " did not dodge!");
                broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " took damage: " + damage + " !");
                pokemonTrainer2.takeDamage(damage);
                if (!pokemonTrainer2.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer2.getName() + "`s " + pokemonTrainer2.getPokemonName() + " is dead!");
                    gameOver = true;
                }
            }

        } else if (pokemonTrainerBattleRound.get(pokemonTrainer1).equals("2") && pokemonTrainerBattleRound.get(pokemonTrainer2).equals("1")) { // Dodge & Attack
            if (randomBoolean(pokemonTrainer1.getDodgeChance())) {
                broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " dodged!");
            } else {
                int damage = pokemonTrainer2.getAttackDamage();
                broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " did not dodge!");
                broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " took damage: " + damage + " !");
                pokemonTrainer1.takeDamage(damage);
                if (!pokemonTrainer1.pokemonIsAlive()) {
                    broadcastMessage(pokemonTrainer1.getName() + "`s " + pokemonTrainer1.getPokemonName() + " is dead!");
                    gameOver = true;
                }
            }

        } else if (pokemonTrainerBattleRound.get(pokemonTrainer1).equals("2") && pokemonTrainerBattleRound.get(pokemonTrainer2).equals("2")) { // Dodge & Dodge
            broadcastMessage("Both PokemonTrainers dodged!");
        }
        if (!gameOver) {
            resetBattleRound();
            broadcastMessage("Next round begins!");
        }
    }

    public void gameOverBroadcast() throws IOException {
        for (IPokemonTrainer pt : pokemonTrainers) {
            if (!pt.pokemonIsAlive()) {
                broadcastMessage(pt.getName() + "`s " + pt.getPokemonName() + " is dead! Game Over! " + pt.getName() + " is running to the next Pokemon Center!");
            }
        }
    }

    IPokemonTrainer randomPokemonTrainer() {
        return pokemonTrainers.get((int) (Math.random() * pokemonTrainers.size()));
    }

    IPokemonTrainer getOtherPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        for (IPokemonTrainer pt : pokemonTrainers) {
            if (!pt.equals(pokemonTrainer)) {
                return pt;
            }
        }
        return null;
    }
    private boolean randomBoolean(int dodgeChance) {
        return Math.random() < 0.1 * dodgeChance;
    }

    private void resetBattleRound() throws IOException {
        pokemonTrainerBattleRound.clear();
        for (IPokemonTrainer pt : pokemonTrainers) {
            pt.setActionStatus(true);
        }
    }

    private void broadcastMessage(String message) throws IOException {
        for (IPokemonTrainer pt : pokemonTrainers) {
            pt.receiveMessage(message);
        }

    }

    private boolean battleRoundReady() throws IOException {
        return pokemonTrainerBattleRound.size() == 2;
    }


    private boolean battleHasStarted() throws IOException {
        return pokemonTrainers.get(0).readyForBattle() && pokemonTrainers.get(1).readyForBattle();
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
        broadcastMessage(pokemonTrainer.getName() + " joined the Arena!");
    }

    @Override
    public void removePokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException {
        this.pokemonTrainers.remove(pokemonTrainer);
        broadcastMessage(pokemonTrainer.getName() + " left the Arena!");
    }

    @Override
    public String getEnemysPokemon(IPokemonTrainer pokemonTrainer) throws IOException {
        return getOtherPokemonTrainer(pokemonTrainer).getPokemonName();
    }

    @Override
    public int getEnemysPokemonHealth(IPokemonTrainer pokemonTrainer) throws IOException {
        return getOtherPokemonTrainer(pokemonTrainer).getPokemonHealth();
    }

}
