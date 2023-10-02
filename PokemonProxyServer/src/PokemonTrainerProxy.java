import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PokemonTrainerProxy implements IPokemonTrainer {

    private final Socket socket;
    private final RpcReader rpcReader;
    private final RpcWriter rpcWriter;


    public PokemonTrainerProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
    }


    public void endConnection() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // End Connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveCommand(String command) throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("1"); // Receive Command
            rpcReader.readLine();
            rpcWriter.println(command);
            String commandResult = rpcReader.readLine();
            if (!commandResult.startsWith("0")) { // Command received
                throw new RuntimeException(commandResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("2"); // Get Trainer Name
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Trainer name received
                return commandResult.substring(2);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
