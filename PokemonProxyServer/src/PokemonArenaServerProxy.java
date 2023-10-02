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
    private Hashtable<IPokemonTrainer, PokemonTrainer> pokemonTrainers = new Hashtable<>();

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
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void leavePokemonArena() {

    }

    private void enterPokemonArena() {
        try {
            PokemonTrainer pokemonTrainer = (PokemonTrainer) objectInputStream.readObject();
            if (this.pokemonTrainers.size() < 3) {
                getPokemonTrainer(pokemonTrainer);
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

    public void getPokemonTrainer(PokemonTrainer pokemonTrainer) throws IOException, ClassNotFoundException {
            rpcWriter.println("0. Pokemon Trainer not in the Pokemon Arena");
            rpcWriter.println("Give me your IP-Adress");
            String ip = rpcReader.readLine();
            rpcWriter.println("Give me your Port");
            String port = rpcReader.readLine();
            Socket s = new Socket(ip, Integer.parseInt(port));
            PokemonTrainerProxy pokemonTrainerProxy = new PokemonTrainerProxy(s);
            this.pokemonTrainers.put(pokemonTrainerProxy, pokemonTrainer);
            rpcWriter.println("0. Pokemon Trainer joined Arena");
    }

}

