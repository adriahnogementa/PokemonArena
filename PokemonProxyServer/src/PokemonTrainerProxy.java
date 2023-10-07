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
        String result = rpcReader.readLine(); // Has Action ?
        return result.startsWith("0");
    }

    @Override
    public void setActionStatus(boolean hasAction) throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("1"); // Set ActionStatus
            rpcReader.readLine();
            if (hasAction) {
                rpcWriter.println("0"); // Set ActionStatus True
            } else {
                rpcWriter.println("9"); // Set ActionStatus False
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    @Override
    public boolean readyForBattle() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("2"); // Ready for Battle
            String result = rpcReader.readLine();
            return result.startsWith("0");  // Ready for Battle

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getPokemonName() throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("3"); // Get Pokemon Name
            String commandResult = rpcReader.readLine();
            if (commandResult.startsWith("0")) { // Pokemon name received
                return commandResult.substring(2);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Missingo";
    }

    @Override
    public int getPokemonHealth() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("12"); // Get Pokemon Health
        String result = rpcReader.readLine();
        return Integer.parseInt(result);
    }


    @Override
    public void receiveMessage(String message) throws IOException {
        try {
            rpcReader.readLine();
            rpcWriter.println("4"); // Receive Message
            rpcReader.readLine();
            rpcWriter.println(message);
            String commandResult = rpcReader.readLine();
            if (!commandResult.startsWith("0")) { // Message received
                throw new RuntimeException(commandResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean pokemonIsAlive() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("7"); // Pokemon is Alive
        String result = rpcReader.readLine();
        return result.startsWith("0");
    }

    @Override
    public void takeDamage(int damage) {
        try {
            rpcReader.readLine();
            rpcWriter.println("9"); // Take Damage
            rpcReader.readLine(); // Enter Damage
            rpcWriter.println(String.valueOf(damage));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public int getAttackDamage() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("11"); // Get Attack Damage
        String result = rpcReader.readLine();
        return Integer.parseInt(result);
    }

    @Override
    public int getInitiative() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("8"); // Get Initiative
        String result = rpcReader.readLine();
        return Integer.parseInt(result);
    }

    @Override
    public int getDodgeChance() throws IOException {
        rpcReader.readLine();
        rpcWriter.println("10"); // Get Dodge Chance
        String result = rpcReader.readLine();
        return Integer.parseInt(result);
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
