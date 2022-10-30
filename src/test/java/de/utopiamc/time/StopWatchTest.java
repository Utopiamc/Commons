package de.utopiamc.time;

import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class StopWatchTest {

    @Mock
    private Logger logger;
    private StopWatch underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new StopWatch(logger);
    }

    @Test
    void itShouldLogMessageWithTime() throws InterruptedException {
        // given
        String message = Mockito.anyString();

        // when
        underTest.start();
        Thread.sleep(2);
        underTest.stopAndLog(message);

        verify(logger).info(message + " " +  2 + "ms");
    }

    @Test
    void itShouldThrowWhenNotStarted() {
        // when
        // then
        assertThatThrownBy(() -> underTest.stopAndLog(""))
                .isExactlyInstanceOf(StopWatchException.class)
                .hasMessageContaining("Tried to stop stopwatch, it it wasn't started!");
    }

    @Test
    void itShouldThrowWhenAlreadyStarted() {
        // when
        underTest.start();

        // then
        assertThatThrownBy(() -> underTest.start())
                .isExactlyInstanceOf(StopWatchException.class)
                .hasMessageContaining("Tried to start stopwatch, but it was already started!");
    }
}