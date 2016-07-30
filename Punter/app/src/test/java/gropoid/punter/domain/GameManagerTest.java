package gropoid.punter.domain;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import gropoid.punter.data.Repository;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;


public class GameManagerTest {

    @Mock
    Context context;
    @Mock
    Repository repository;

    GameManager gameManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        gameManager = new GameManager(context, repository);
    }

    @Test
    public void testGetImageExtension() throws Exception {
        // GIVEN
        String url = "https://www.giantbomb.com/api/image/square_mini/2370498-genesis_desertstrike_2__1_.jpg";

        // WHEN
        String extension = gameManager.getImageExtension(url);

        // THEN
        assertThat(extension).isEqualTo(".jpg");
    }
}