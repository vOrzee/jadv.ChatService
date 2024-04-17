import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.server.ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ChatServerTests {
    private ServerSocket serverSocketMock;
    private Socket socketMock;
    private ChatServer chatServer;

    @BeforeEach
    void setUp() throws Exception {
        serverSocketMock = mock(ServerSocket.class);
        socketMock = mock(Socket.class);
        when(serverSocketMock.accept()).thenReturn(socketMock);
        chatServer = new ChatServer() {
            @Override
            protected ServerSocket createServerSocket(int port) {
                return serverSocketMock;
            }
        };
    }

    @Test
    void testAcceptsConnection() throws Exception {
        Thread serverThread = new Thread(() -> chatServer.start());
        serverThread.start();

        verify(serverSocketMock, timeout(1000).atLeastOnce()).accept();
        serverThread.interrupt();
    }

    @Test
    void testHandleIOExceptionOnSocket() throws Exception {
        when(serverSocketMock.accept()).thenThrow(new IOException("Test exception"));

        Thread serverThread = new Thread(() -> chatServer.start());
        serverThread.start();

        verify(serverSocketMock, timeout(1000).atLeastOnce()).accept();
        serverThread.interrupt();
    }


}
