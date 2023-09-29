import java.io.PrintWriter;
import java.io.Writer;

public class RpcWriter extends PrintWriter {
    public RpcWriter(Writer out) {
        super(out);
    }

    @Override
    public void println(String x) {
        super.println(x);
        super.flush();
    }
}