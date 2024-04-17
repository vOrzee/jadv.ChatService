import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.models.Chat;
import ru.netology.models.User;
import ru.netology.repository.Repository;
import ru.netology.repository.RepositoryInMemory;

import java.io.PrintWriter;
import java.io.StringWriter;

class RepositoryInMemoryTests {
    private Repository repository;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        repository = RepositoryInMemory.getInstance();
        StringWriter stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    @Test
    void testCreateAndRetrieveUser() {
        String userName = "testUser";
        User user = repository.createUser(userName, writer);
        assertNotNull(user, "User should not be null");
        assertEquals(userName, user.getName(), "User name should match the provided name");

        User retrievedUser = repository.getUserByName(userName);
        assertNotNull(retrievedUser, "Retrieved user should not be null");
        assertEquals(user, retrievedUser, "Retrieved user should be the same as the created user");
    }

    @Test
    void testCreateAndRetrieveChat() {
        User user1 = repository.createUser("user1", writer);
        User user2 = repository.createUser("user2", writer);
        Chat chat = repository.createChat(user1, user2);
        assertNotNull(chat, "Chat should not be null");

        Chat retrievedChat = repository.getChatBetweenUsers(user1, user2);
        assertNotNull(retrievedChat, "Retrieved chat should not be null");
        assertEquals(chat, retrievedChat, "Retrieved chat should be the same as the created chat");
    }

    @Test
    void testUserNotFound() {
        assertNull(repository.getUserByName("nonexistent"), "Should return null for nonexistent user");
    }
}