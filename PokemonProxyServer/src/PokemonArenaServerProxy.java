import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class PokemonArenaServerProxy implements Runnable{

    private Socket socket;
    private IPokemonArena pokemonArena;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private ArrayList<IPokemonTrainer> pokemonTrainers = new ArrayList<>();
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
        while (isRunning){
            rpcWriter.println("1. Send Command ; 2. Enter Arena ; 3. Leave Arena  ; 4. End the Connection");
            String input = rpcReader.readLine();
     switch (input){
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
     }catch (IOException e){
         throw new RuntimeException(e.getMessage());
     }
    }

    private void leavePokemonArena() {
    }

    private void enterPokemonArena() {

    }

    private void endConnection() throws IOException {
    this.isRunning = false;
    this.socket.close();
    }


    private void sendCommand() {
    }


}
