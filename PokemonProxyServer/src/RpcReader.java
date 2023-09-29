import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class RpcReader extends BufferedReader {
    public RpcReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        return super.readLine();
    }
}