package CLI.Main;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


public class MainTest {

    CLI cli;

    @Test
    void mainRan() {
        Main main = new Main();

        cli = mock(CLI.class);

        verify(cli, times(1)).run();
    }

}
