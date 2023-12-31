import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        PokemonArena pokemonArena = new PokemonArena();
        while(true) {
            Socket socket = serverSocket.accept();
            PokemonArenaServerProxy pokemonArenaServerProxy = new PokemonArenaServerProxy(socket, pokemonArena);
            Thread thread = new Thread(pokemonArenaServerProxy);
            thread.start();
        }
    }
}