import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PokemonTrainerProxy implements IPokemonTrainer {

    private Socket socket;
    private RpcReader rpcReader;
    private RpcWriter rpcWriter;


    public PokemonTrainerProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void receiveCommand(String command) throws IOException {

        rpcReader.readLine();
        rpcWriter.println("Command received");
        rpcReader.readLine();
        rpcWriter.println(command);

    }

    @Override
    public String name() {
        try {
            rpcReader.readLine();
            rpcWriter.println("Give me your name");
            rpcReader.readLine();
            rpcWriter.println("My name is " + rpcReader.readLine());
            return rpcReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void endConnection() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("End the Connection");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
