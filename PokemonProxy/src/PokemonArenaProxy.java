import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PokemonArenaProxy implements IPokemonArena{
    private Socket socket;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private final PokemonTrainer pokemonTrainer;
    private boolean pokemonArenaIsRunning = false;


    public PokemonArenaProxy(Socket socket, PokemonTrainer pokemonTrainer) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.pokemonTrainer = pokemonTrainer;
    }
    @Override
    public void sendCommand(String command, IPokemonTrainer pokomonTrainer) {
        try {
            rpcReader.readLine();
            rpcWriter.println("Send Command");
            rpcReader.readLine();
            rpcWriter.println(command);
            sendPokemonTrainer(pokomonTrainer);
            String commandResult = rpcReader.readLine();
            if(commandResult.equals("Command received")){
                System.out.println("Command received");}
            else{throw new RuntimeException("Error while sending the command");}

        } catch (IOException e) {
           throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void sendPokemonTrainer(IPokemonTrainer pokomonTrainer) {
        try{
            if (!pokemonArenaIsRunning){
                rpcReader.readLine(); // Give me your IP-Adress
                rpcWriter.println(Utility.getLocalIpAddress());
                rpcReader.readLine(); // Give me your Port
                ServerSocket serverSocket = new ServerSocket(0);
                rpcWriter.println(serverSocket.getLocalPort());
                Socket socket = serverSocket.accept();
                PokemonTrainerServerProxy pokemonTrainerServerProxy = new PokemonTrainerServerProxy(pokomonTrainer, socket);
                Thread thread = new Thread(pokemonTrainerServerProxy);
                thread.start();
                this.pokemonArenaIsRunning = true;
            }else {
                rpcReader.readLine();
                rpcWriter.println(this.pokemonTrainer);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void enterPokemonArena(IPokemonTrainer pokomonTrainer) {
        try{
            rpcReader.readLine();
            rpcWriter.println("Enter Pokemon Arena");
            sendPokemonTrainer(pokomonTrainer);
            String commandResult = rpcReader.readLine();
            if(commandResult.equals("Pokemon Arena entered")){
                System.out.println("Pokemon Arena entered");}
            else{throw new RuntimeException("Error while entering the Pokemon Arena");}
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void exitPokemonArena(IPokemonTrainer pokomonTrainer) {

        try{
            rpcReader.readLine();
            rpcWriter.println("Exit Pokemon Arena");
            sendPokemonTrainer(pokomonTrainer);
            String commandResult = rpcReader.readLine();
            if(commandResult.equals("Pokemon Arena exited")){
                System.out.println("Pokemon Arena exited");}
            else{throw new RuntimeException("Error while exiting the Pokemon Arena");}
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void endConnection() {
        this.pokemonArenaIsRunning = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
