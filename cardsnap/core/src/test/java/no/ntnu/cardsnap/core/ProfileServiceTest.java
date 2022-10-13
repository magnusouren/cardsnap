package no.ntnu.cardsnap.core;

import no.ntnu.cardsnap.domain.Card;
import no.ntnu.cardsnap.domain.CardDeck;
import no.ntnu.cardsnap.domain.Profile;
import no.ntnu.cardsnap.persistence.DiskProfileStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProfileServiceTest {
    private final DiskProfileStorage storage = new DiskProfileStorage("/tmp/cardsnap-test");
    private final ProfileService service = new ProfileService(storage);

    @AfterEach
    public void setup() {
        storage.getStoragePath().toFile().delete();
    }

    @Test
    @DisplayName("it can load an existing profile from storage")
    public void testLoadFromService() {
        Profile p = service.load();
        assertNotNull(p);

        // Cause circumstance under which storage.load will fail
        storage.getStoragePath().toFile().setReadable(false);
        assertThrows(IllegalStateException.class, service::load);
    }

    @Test
    @DisplayName("it can store existing profiles to storage")
    public void testStoreToStorage() {
        Profile p = service.load();
        service.store(p);

        // Cause circumstance under which storage.store will fail
        storage.getStoragePath().toFile().setWritable(false);
        assertThrows(IllegalStateException.class, () -> service.store(p));
    }

    @Test
    @DisplayName("it can create new card decks in a profile")
    public void testCreateDeckInProfile() {
        Profile p = service.load();
        CardDeck created = service.create(p, "New Deck");
        assertEquals("New Deck", created.getName());

        assertThrows(IllegalArgumentException.class, () -> service.create(p, "New Deck"));
    }

    @Test
    @DisplayName("it can create new cards in a deck")
    public void testCreateCardInDeckInProfile() {
        Profile p = service.load();
        CardDeck root = service.create(p, "root");
        Card created = service.create(p, root, "foo", "bar");
        assertEquals("foo", created.getQuestion());
        assertEquals("bar", created.getAnswer());

        assertThrows(IllegalArgumentException.class, () -> service.create(p, root, "foo", "bar"));
    }
}