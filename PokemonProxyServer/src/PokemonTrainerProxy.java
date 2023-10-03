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
            rpcWriter.println("5"); // End Connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasAction() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("0"); // Has Action
        String result = rpcReader.readLine();
        if (result.startsWith("0")) { // Has Action
            return true;
        } else if (result.startsWith("9")) {
            return false;
        }
        return false;
    }

    @Override
    public void setActionStatus(boolean hasAction) throws IOException {
        try {
         rpcReader.readLine();
            if (hasAction) {
                rpcWriter.println("1"); // Set ActionStatus True
            } else {
                rpcWriter.println("9"); // Set ActionStatus False
            }
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    @Override
    public boolean readyForBattle() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("2"); // Ready for Battle
            String result = rpcReader.readLine();
            if (result.startsWith("0")) { // Ready for Battle
                return true;
            } else if (result.startsWith("9")) {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }

    @Override
    public void receiveMessage(String command) throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("4"); // Receive Command
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
    public void receiveCommand(String command) throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // Receive Command
            rpcReader.readLine(); // Enter your command
            rpcWriter.println(command);


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public String getName() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("5"); // Get Trainer Name
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
