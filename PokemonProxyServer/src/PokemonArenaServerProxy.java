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
       try {
           pokemonArena.removePokemonTrainer(getPokemonTrainer());
           rpcWriter.println("0. Pokemon Trainer left Arena");
       }catch (Exception e){
           e.printStackTrace();
           rpcWriter.println("9. Error while leaving the Pokemon Arena");
       }
    }

    private void enterPokemonArena() {
        try {
            if (this.pokemonTrainers.size() < 3) {
                pokemonArena.addPokemonTrainer(getPokemonTrainer());
                rpcWriter.println("0. Pokemon Trainer entered Arena");
            } else {
                rpcWriter.println("9. Pokemon Arena full");
            }
        } catch (IOException  e) {
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

    public IPokemonTrainer getPokemonTrainer() throws IOException {
            rpcWriter.println("Give me your Trainer");
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
            }
            return pokemonTrainers.get(trainerId);
    }


}

