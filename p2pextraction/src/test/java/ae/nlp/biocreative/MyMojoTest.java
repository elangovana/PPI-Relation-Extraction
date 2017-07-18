package ae.nlp.biocreative;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by aparnaelangovan on 17/07/2017.
 */
class MyMojoTest {
    private MyMojo sut;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
       sut = new MyMojo();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.Test
    void execute() {
        sut.execute();
    }

}