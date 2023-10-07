import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PokemonTrainerServerProxy implements Runnable {

    private final IPokemonTrainer pokemonTrainer;
    private final Socket socket;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private boolean isRunning = true;

    public PokemonTrainerServerProxy(IPokemonTrainer pokemonTrainer, Socket socket) {
        this.pokemonTrainer = pokemonTrainer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
            rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
            while (isRunning) {

                rpcWriter.println("Chose between 0 - 11");
                String input = rpcReader.readLine();

                switch (input) {
                    case "0":
                        hasAction();
                        break;
                    case "1":
                        setActionStatus();
                        break;
                    case "2":
                        readyForBattle();
                        break;
                        case "3":
                            getPokemonName();
                            break;
                    case "4":
                        receiveMessage();
                        break;
                    case "5":
                        getTrainerName();
                        break;
                    case "6":
                        endConnection();
                        break;
                    case "7":
                        isAlive();
                        break;
                    case "8":
                        getInitiative();
                        break;
                    case "9":
                        takeDamage();
                        break;
                    case "10":
                        getDodgeChance();
                        break;
                    case "11":
                        getAttackDamage();
                        break;
                    default:
                        rpcWriter.println("Invalid input");
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getPokemonName() {
        try {
            String pokemonName = this.pokemonTrainer.getPokemonName();
            rpcWriter.println("0." + pokemonName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getAttackDamage() {
        try {
            int attackDamage = this.pokemonTrainer.getAttackDamage();
            rpcWriter.println(attackDamage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getDodgeChance() {
        try {
            int dodgeChance = this.pokemonTrainer.getDodgeChance();
            rpcWriter.println(dodgeChance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void takeDamage() {
        try {
            rpcWriter.println("Enter your Damage");
            String damage = rpcReader.readLine();
            this.pokemonTrainer.takeDamage(Integer.parseInt(damage));
        } catch (IOException e) {
            e.printStackTrace();
            rpcWriter.println("1. Error while reading the command");
        }
    }

    private void getInitiative() {
        try {
            int initiative = this.pokemonTrainer.getInitiative();
            rpcWriter.println(initiative);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void isAlive() {
         if (pokemonTrainer.pokemonIsAlive()) {
                rpcWriter.println("0. Pokemon is alive");
            } else {
                rpcWriter.println("9. Pokemon is dead");
            }
    }

    private void setActionStatus() throws IOException {
        rpcWriter.println("Enter your Status Change");
        String status = rpcReader.readLine();
        if (status.startsWith("0")) {
            this.pokemonTrainer.setActionStatus(true);
        } else if (status.startsWith("9")) {
            this.pokemonTrainer.setActionStatus(false);
        }
    }

    private void hasAction() {
        try {
            if (pokemonTrainer.hasAction()) {
                rpcWriter.println("0. Action possible");
            } else {
                rpcWriter.println("9. No action possible");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readyForBattle() {
        try {
            if (pokemonTrainer.readyForBattle()) {
                rpcWriter.println("0. Ready for Battle");
            } else {
                rpcWriter.println("9. Not ready for Battle");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private void receiveMessage() {
        try {
            rpcWriter.println("Enter your Message");
            String message = rpcReader.readLine();
            this.pokemonTrainer.receiveMessage(message);
            rpcWriter.println("0. Command Message");
        } catch (IOException e) {
            e.printStackTrace();
            rpcWriter.println("1. Error while reading the command");
        }
    }

    private void getTrainerName() {
        try {
            String trainerName = this.pokemonTrainer.getName();
            rpcWriter.println("0." + trainerName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void endConnection() {
        this.isRunning = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

