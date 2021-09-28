package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final String LOCALHOST = "127.0.0.1";
    public static final int SERVER_PORT = 4444;

    public static void main(String[] args) {
        try {
            final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(LOCALHOST, SERVER_PORT));
            end:
            while (true) {
                try (SocketChannel socketChannel = serverSocketChannel.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break end;
                        final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        System.out.println("Получена строка от клиента: " + msg);
                        String newMsg = getNewString(msg);
                        socketChannel.write(ByteBuffer.wrap(("Строка без пробелов: "
                                + newMsg).getBytes(StandardCharsets.UTF_8)));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getNewString(String msg) {
        String[] arrayMsg = msg.split(" ");
        String newMsg = "";
        for (int i = 0; i < arrayMsg.length; i++)
            newMsg = newMsg.concat(arrayMsg[i]);
        //System.out.println(newMsg);
        return newMsg;
    }
}
