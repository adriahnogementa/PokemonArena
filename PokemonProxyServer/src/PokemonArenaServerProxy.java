import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Hashtable;

public class PokemonArenaServerProxy implements Runnable {

    private final Socket socket;
    private final IPokemonArena pokemonArena;
    private ObjectInputStream objectInputStream;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private boolean isRunning = true;
    private final Hashtable<PokemonTrainer, IPokemonTrainer> pokemonTrainers = new Hashtable<>();

    public PokemonArenaServerProxy(Socket socket, IPokemonArena pokemonArena) {
        this.socket = socket;
        this.pokemonArena = pokemonArena;
    }


    @Override
    public void run() {
        try {
            this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (isRunning) {
                rpcWriter.println("1. Send Command ; 2. Enter Arena ; 3. Leave Arena  ; 4. End the Connection");
                String input = rpcReader.readLine();
                switch (input) {
                    case "1":
                        sendCommand();
                        break;
                    case "2":
                        enterPokemonArena();
                        break;
                    case "3":
                        leavePokemonArena();
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

    private void leavePokemonArena() throws IOException, ClassNotFoundException {
        PokemonTrainer pokemonTrainer = (PokemonTrainer) objectInputStream.readObject();
        if (this.pokemonTrainers.containsKey(pokemonTrainer)) {
            this.pokemonArena.removePokemonTrainer(pokemonTrainers.get(pokemonTrainer));
            this.pokemonTrainers.remove(pokemonTrainer);
            rpcWriter.println("0. Pokemon Trainer left Arena");
        } else {
            rpcWriter.println("9. Pokemon Trainer not in the Pokemon Arena");
        }


    }

    private void enterPokemonArena() {
        try {
            PokemonTrainer pokemonTrainer = (PokemonTrainer) objectInputStream.readObject();
            if (this.pokemonTrainers.size() < 3) {
                establishConnectionToPokemonTrainerServerProxy(pokemonTrainer);
                this.pokemonArena.addPokemonTrainer(pokemonTrainers.get(pokemonTrainer));
            }else {
                rpcWriter.println("9. Pokemon Arena full");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            rpcWriter.println("Error while entering the Pokemon Arena");
        }

    }

    private void endConnection() throws IOException {
        this.isRunning = false;
        this.socket.close();
    }


    private void sendCommand() throws IOException {

    }

    public void establishConnectionToPokemonTrainerServerProxy(PokemonTrainer pokemonTrainer) throws IOException, ClassNotFoundException {
            rpcWriter.println("Give me your IP-Adress");
            String ip = rpcReader.readLine();
            rpcWriter.println("Give me your Port");
            String port = rpcReader.readLine();
            Socket s = new Socket(ip, Integer.parseInt(port));
            PokemonTrainerProxy pokemonTrainerProxy = new PokemonTrainerProxy(s);
            this.pokemonTrainers.put(pokemonTrainer, pokemonTrainerProxy);
            rpcWriter.println("0. Pokemon Trainer joined Arena");
    }


}

