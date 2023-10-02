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

    }

    @Override
    public void receiveCommand(String command) throws IOException {

    }

    @Override
    public String getName() throws IOException {
        return null;
    }
}
