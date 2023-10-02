import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PokemonTrainerServerProxy implements Runnable {

    private IPokemonTrainer pokemonTrainer;
    private Socket socket;
    private RpcWriter rpcWriter;
    private RpcReader rpcReader;
    private boolean isRunning = true;

    public PokemonTrainerServerProxy(IPokemonTrainer pokemonTrainer, Socket socket) {
        this.pokemonTrainer = pokemonTrainer;
        this.socket = socket;
    }

    @Override
    public void run() {
   try{
       rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
       rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
       while(isRunning) {

           rpcWriter.println("1. Receive Command ; 2. Get Trainer Name ; 3. End the Connection");
           String input = rpcReader.readLine();

           switch (input) {
               case "1":
                   receiveCommand();
                   break;
                case "2":
                    getTrainerName();
                    break;
               case "3":
                   endConnection();
                   break;
               default:
                   rpcWriter.println("Invalid input");
                   break;
           }
       }
    } catch (IOException e) {
       throw new RuntimeException(e);
   }

}

    private void getTrainerName() {
        try {
            String trainerName = this.pokemonTrainer.getName();
            rpcWriter.println("0."+ trainerName);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private void receiveCommand(){
        rpcWriter.println("Enter the command: 1. Attack ; 2. Heal");
        try {
            String command = rpcReader.readLine();
            this.pokemonTrainer.receiveCommand(command);
            rpcWriter.println("0. Command received");
        } catch (IOException e) {
            e.printStackTrace();
            rpcWriter.println("1. Error while reading the command");
        }
    }

    private void endConnection() {
        this.isRunning = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
