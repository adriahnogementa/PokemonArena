import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;

public class PokemonArenaProxy implements IPokemonArena {
    private final Socket socket;
    private final RpcWriter rpcWriter;
    private final RpcReader rpcReader;
    private Hashtable<IPokemonTrainer, String> pokemonTrainers = new Hashtable<>();
    private int maxId= 0;


    public PokemonArenaProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) {

    }

    @Override
    public void addPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            rpcReader.readLine();
            rpcWriter.println("2"); // Enter Pokemon Arena
            sendPokemonTrainer(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena entered
                System.out.println("PokemonTrainer joined Arena");
            } else if (commandResult.startsWith("9")) {
                System.out.println("Pokemontrainer already in Arena");
            } else if (commandResult.startsWith("9")) {
                System.out.println("PokemonTrainer can't join Arena");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void removePokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // Leave Pokemon Arena
            sendPokemonTrainer(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena left
                System.out.println("PokemonTrainer left Arena");
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

    private void sendPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException {
        String ret = pokemonTrainers.get(pokemonTrainer);
        if (ret == null) {
            maxId++;
            pokemonTrainers.put(pokemonTrainer, String.valueOf(maxId));
            String id = pokemonTrainers.get(pokemonTrainer);
            rpcReader.readLine(); // Give me your ID
            rpcWriter.println(id);
            rpcReader.readLine(); // Give me your IP-Adress
            rpcWriter.println(Utility.getLocalIpAddress());
            rpcReader.readLine(); // Give me your Port
            ServerSocket serverSocket = new ServerSocket(0);
            rpcWriter.println(String.valueOf(serverSocket.getLocalPort()));
            Socket socket = serverSocket.accept();
            PokemonTrainerServerProxy pokemonTrainerServerProxy = new PokemonTrainerServerProxy(pokemonTrainer, socket);
            Thread thread = new Thread(pokemonTrainerServerProxy);
            thread.start();
        }else {
            String id = pokemonTrainers.get(pokemonTrainer);
            rpcReader.readLine();
            rpcWriter.println(id);

        }
    }

}
