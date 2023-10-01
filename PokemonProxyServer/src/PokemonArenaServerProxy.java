import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PokemonArenaServerProxy implements Runnable {

    private Socket socket;
    private IPokemonArena pokemonArena;
    private ObjectInputStream objectInputStream;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private boolean isRunning = true;

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
        try {
            if (!this.pokemonArena.getPokemonTrainers().isEmpty()) {
                this.pokemonArena.getPokemonTrainers().remove((IPokemonTrainer) objectInputStream.readObject());
                rpcWriter.println("Pokemon Arena left");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            rpcWriter.println("Error while leaving the Pokemon Arena");
        }
    }

    private void enterPokemonArena() {
        try {
            IPokemonTrainer pokemonTrainer = (IPokemonTrainer) objectInputStream.readObject();
            getPokemonTrainer(pokemonTrainer);
            if (this.pokemonArena.getPokemonTrainers().size() < 2) {
                this.pokemonArena.getPokemonTrainers().add(pokemonTrainer);
                rpcWriter.println("Pokemon Arena entered");
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

        rpcWriter.println("Enter the command");
        String command = rpcReader.readLine();
        rpcWriter.println("Command received");
        try {
            this.pokemonArena.sendCommand(command, (IPokemonTrainer) objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            rpcWriter.println("Error while sending the command");
        }
    }

    public IPokemonTrainer getPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException, ClassNotFoundException {
        if(!this.pokemonArena.getPokemonTrainers().contains(pokemonTrainer)) {
            rpcWriter.println("Gib mir deine IP");
            String ip = rpcReader.readLine();
            rpcWriter.println("Gib mir deinen Port");
            String port = rpcReader.readLine();
            Socket s = new Socket(ip, Integer.parseInt(port));
            PokemonTrainerProxy pokemonTrainerProxy = new PokemonTrainerProxy(s);
            this.pokemonArena.getPokemonTrainers().add(pokemonTrainerProxy);

        }
        return pokemonTrainer;
    }

}

