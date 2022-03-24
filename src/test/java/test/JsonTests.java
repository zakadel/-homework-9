package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTests {
    private static final String JSON_FILE_PATH = "src/test/resources/jsonFile.json";

    @Test
    void verifyJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        PersonTest person = mapper.readValue(Paths.get(JSON_FILE_PATH).toFile(), PersonTest.class);
        assertThat(person.name).isEqualTo("Adel");
        assertThat(person.lastname).isEqualTo("Zakiev");
    }
}
