import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class PokemonArenaProxy implements IPokemonArena {
    private final Socket socket;
    private final RpcWriter rpcWriter;
    private final ObjectOutputStream objectOutputStream;
    private final RpcReader rpcReader;
    private boolean joinedArena = false;


    public PokemonArenaProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) {

    }

    private void sendPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            objectOutputStream.writeObject(pokemonTrainer);
            rpcReader.readLine(); // Give me your IP-Adress
            rpcWriter.println(Utility.getLocalIpAddress());
            rpcReader.readLine(); // Give me your Port
            ServerSocket serverSocket = new ServerSocket(0);
            rpcWriter.println(String.valueOf(serverSocket.getLocalPort()));
            Socket socket = serverSocket.accept();
            PokemonTrainerServerProxy pokemonTrainerServerProxy = new PokemonTrainerServerProxy(pokemonTrainer, socket);
            Thread thread = new Thread(pokemonTrainerServerProxy);
            thread.start();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            if (this.joinedArena) {
                System.out.println("PokemonTrainer already joined Arena");
                return;
            }
            rpcReader.readLine();
            rpcWriter.println("2"); // Enter Pokemon Arena
            sendPokemonTrainer(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena entered
                System.out.println("PokemonTrainer joined Arena");
                this.joinedArena = true;
            } else if (commandResult.startsWith("9")) {
                System.out.println("PokemonTrainer can't join Arena");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void removePokemonTrainer(IPokemonTrainer pokemonTrainer) {
        if (!this.joinedArena) {
            System.out.println("PokemonTrainer not joined Arena");
            return;
        }
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // Leave Pokemon Arena
            objectOutputStream.writeObject(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena left
                System.out.println("PokemonTrainer left Arena");
                this.joinedArena = false;
            } else if (commandResult.startsWith("9")) {
                System.out.println("PokemonTrainer not in Arena");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    public void endConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
