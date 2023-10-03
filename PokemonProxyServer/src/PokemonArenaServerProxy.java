import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Hashtable;

public class PokemonArenaServerProxy implements Runnable {

    private final Socket socket;
    private final IPokemonArena pokemonArena;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private boolean isRunning = true;
    private final Hashtable<String, IPokemonTrainer> pokemonTrainers = new Hashtable<>();

    public PokemonArenaServerProxy(Socket socket, IPokemonArena pokemonArena) {
        this.socket = socket;
        this.pokemonArena = pokemonArena;
    }


    @Override
    public void run() {
        try {
            this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
            while (isRunning) {
                rpcWriter.println("1. Start Battle ; 2. Enter Arena ; 3. Leave Arena  ; 4. End the Connection");
                String input = rpcReader.readLine();
                switch (input) {
                    case "0":
                        sendCommand();
                        break;
                    case "1":
                        startBattle();
                        break;
                    case "2":
                        addPokemonTrainer();
                        break;
                    case "3":
                        removePokemonTrainer();
                        break;
                    case "4":
                        endConnection();
                        break;
                    default:
                        rpcWriter.println("Invalid input");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendCommand() {
        try {
            String trainerId = rpcReader.readLine();
            IPokemonTrainer pokemonTrainer = pokemonTrainers.get(trainerId);
            rpcWriter.println("0. Send Command");
            String command = rpcReader.readLine();
            pokemonArena.sendCommand(command, pokemonTrainer);
            rpcWriter.println("0. Command sent");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startBattle() {
            try {
                if(!pokemonArena.startBattle()){
                    rpcWriter.println("9. Arena is Empty");
                }else {
                    rpcWriter.println("0. Battle will start soon");
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    private void removePokemonTrainer() throws IOException, ClassNotFoundException {
        try {
            IPokemonTrainer pokemonTrainer = getPokemonTrainerForLeavingArena();
            if (pokemonTrainer == null) {
                return;
            }
            pokemonArena.removePokemonTrainer(pokemonTrainer);
            rpcWriter.println("0. Pokemon Trainer left Arena");
        } catch (Exception e) {
            e.printStackTrace();
            rpcWriter.println("9. Error while leaving the Pokemon Arena");
        }
    }

    private void addPokemonTrainer() {
        try {
            if (this.pokemonTrainers.size() <= 2) {
                pokemonArena.addPokemonTrainer(getPokemonTrainer());
            } else {
                rpcWriter.println("9. Pokemon Arena full");
            }
        } catch (IOException e) {
            e.printStackTrace();
            rpcWriter.println("Error while entering the Pokemon Arena");
        }

    }

    private void endConnection() throws IOException {
        this.isRunning = false;
        this.socket.close();
    }

    private IPokemonTrainer getPokemonTrainer() throws IOException {
        String trainerId = rpcReader.readLine();
        IPokemonTrainer ret = pokemonTrainers.get(trainerId);
        if (ret == null) {
            rpcWriter.println("Give me your IP-Adress");
            String ip = rpcReader.readLine();
            rpcWriter.println("Give me your Port");
            String port = rpcReader.readLine();
            Socket s = new Socket(ip, Integer.parseInt(port));
            PokemonTrainerProxy pokemonTrainerProxy = new PokemonTrainerProxy(s);
            this.pokemonTrainers.put(trainerId, pokemonTrainerProxy);
            rpcWriter.println("0. Pokemon Trainer entered Arena");
        } else {
            rpcWriter.println("8. Pokemon Trainer already in Arena");
        }
        return pokemonTrainers.get(trainerId);
    }

    private IPokemonTrainer getPokemonTrainerForLeavingArena() throws IOException {
        String trainerId = rpcReader.readLine();
        IPokemonTrainer ret = pokemonTrainers.get(trainerId);
        if (ret == null) {
            rpcWriter.println("9. Pokemon Trainer not in Arena");
            return null;
        } else {
            rpcWriter.println("0. Pokemon Trainer left Arena");
            IPokemonTrainer pokemonTrainer = pokemonTrainers.get(trainerId);
            pokemonTrainers.remove(trainerId);
            return pokemonTrainer;
        }
    }

}

