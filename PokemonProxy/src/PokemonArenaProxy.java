import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class PokemonArenaProxy implements IPokemonArena{
    private final Socket socket;
    private final RpcWriter rpcWriter;
    private final ObjectOutputStream objectOutputStream;
    private final RpcReader rpcReader;
    private final PokemonTrainer pokemonTrainer;



    public PokemonArenaProxy(Socket socket, PokemonTrainer pokemonTrainer) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.pokemonTrainer = pokemonTrainer;
    }
    @Override
    public void sendCommand(String command, IPokemonTrainer pokomonTrainer) {

    }

    private void sendPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try{
            objectOutputStream.writeObject(pokemonTrainer);
            String result = rpcReader.readLine();
            if (result.startsWith("0")){ // PokemonTrainer can join Arena
                rpcReader.readLine(); // Give me your IP-Adress
                rpcWriter.println(Utility.getLocalIpAddress());
                rpcReader.readLine(); // Give me your Port
                ServerSocket serverSocket = new ServerSocket(0);
                rpcWriter.println(String.valueOf(serverSocket.getLocalPort()));
                Socket socket = serverSocket.accept();
                PokemonTrainerServerProxy pokemonTrainerServerProxy = new PokemonTrainerServerProxy(pokemonTrainer, socket);
                Thread thread = new Thread(pokemonTrainerServerProxy);
                thread.start();
                System.out.println("PokemonTrainer joined Arena");
            }else if(result.startsWith("9")){ // PokemonTrainer can't join Arena
                System.out.println("PokemonTrainer can't join Arena");}
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void enterPokemonArena(IPokemonTrainer pokemonTrainer) {
        try{
            rpcReader.readLine();
            rpcWriter.println("2"); // Enter Pokemon Arena
            sendPokemonTrainer(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if(commandResult.startsWith("9")){ // Pokemon Arena entered
                System.out.println("Pokemon Arena is full");}
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void exitPokemonArena(IPokemonTrainer pokemonTrainer) {

    }


    public void endConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
