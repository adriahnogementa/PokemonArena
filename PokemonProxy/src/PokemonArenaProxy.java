import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class PokemonArenaProxy implements IPokemonArena {
    private final Socket socket;
    private final RpcWriter rpcWriter;
    private final RpcReader rpcReader;
    private final Hashtable<IPokemonTrainer, String> pokemonTrainers = new Hashtable<>();
    private int maxId= 0;

    public PokemonArenaProxy(Socket socket) throws IOException {
        this.socket = socket;
        this.rpcReader = new RpcReader(new InputStreamReader(socket.getInputStream()));
        this.rpcWriter = new RpcWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    //This method is used to send the command to the server
    //If the command is 1, the PokemonTrainer wants to attack
    //If the command is 2, the PokemonTrainer wants to dodge
    @Override
    public void sendCommand(String command, IPokemonTrainer pokemonTrainer) throws IOException {
        rpcReader.readLine();
        rpcWriter.println("0");
        String id = pokemonTrainers.get(pokemonTrainer);
        rpcWriter.println(id);
        rpcReader.readLine();
        if (command.startsWith("1")) {
            rpcWriter.println("1");

        } else if (command.startsWith("2")) {
            rpcWriter.println("2");

        }
        String commandResult = rpcReader.readLine();
        if (!commandResult.startsWith("0")) { // Command received
            System.out.println("Command not received");
        }
    }

    //Battle starts only if there are at least 2 PokemonTrainers in the Arena
    //If there are less than 2 PokemonTrainers, the Battle not start
    @Override
    public boolean startBattle() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("1"); // Start Battle
        String commandResult = rpcReader.readLine();
        if (commandResult.startsWith("0")) { // Battle will start soon
            return true;
        } else if (commandResult.startsWith("9")) {
            System.out.println("Arena is empty! Wait for other PokemonTrainers to join the Arena");
            return false;
        }
        return true;
    }

    @Override
    public void addPokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            rpcReader.readLine();
            rpcWriter.println("2"); // Enter Pokemon Arena
            sendPokemonTrainer(pokemonTrainer);
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena entered
                System.out.println("Pokemontrainer joined Arena");
            } else if (commandResult.startsWith("8")) {
                System.out.println("Pokemontrainer already in Arena");
            } else if (commandResult.startsWith("9")) {
                System.out.println("Pokemontrainer can't join Arena");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    //This method is used to send the PokemonTrainer to the server
    //For Removing the PokemonTrainer from the Server and the Hashtable
    //If the Server knows the PokemonTrainer, nothing happens
    @Override
    public void removePokemonTrainer(IPokemonTrainer pokemonTrainer) {
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // Leave Pokemon Arena
            String ret = pokemonTrainers.get(pokemonTrainer);
            rpcWriter.println(Objects.requireNonNullElse(ret, "-1"));
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon Arena left
                System.out.println("PokemonTrainer left Arena");
                this.pokemonTrainers.remove(pokemonTrainer);
            }else if (commandResult.startsWith("9")) {
                System.out.println("PokemonTrainer can't leave Arena. Join the Arena first");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public String getEnemysPokemon(IPokemonTrainer pokemonTrainer) throws IOException {
        rpcReader.readLine();
        rpcWriter.println("5"); // Get Enemys Pokemon
        String ret = pokemonTrainers.get(pokemonTrainer);
        rpcWriter.println(ret);
        return rpcReader.readLine(); // Enemys Pokemon
    }

    @Override
    public int getEnemysPokemonHealth(IPokemonTrainer pokemonTrainer) throws IOException {
        rpcReader.readLine();
        rpcWriter.println("6"); // Get Enemys Pokemon Health
        String ret = pokemonTrainers.get(pokemonTrainer);
        rpcWriter.println(ret);
        String health = rpcReader.readLine();
        return Integer.parseInt(health); // Enemys Pokemon Health
    }


    public void endConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This method is used to send the PokemonTrainer to the server
    //If the PokemonTrainer is already known, the ID is sent
    //If the PokemonTrainer is unknown, the PokemonTrainer is sent to the server
    private void sendPokemonTrainer(IPokemonTrainer pokemonTrainer) throws IOException {
        String ret = pokemonTrainers.get(pokemonTrainer);
        if (ret == null) {
            maxId++;
            pokemonTrainers.put(pokemonTrainer, String.valueOf(maxId));
            String id = pokemonTrainers.get(pokemonTrainer);
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
            rpcWriter.println(id);

        }
    }

}
