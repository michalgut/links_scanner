package com.jamfnow.qa.links;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SystemTest {

    @Test
    public void testAgainstNomad() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test_nomad.yml").getFile());
        LinksConfig linksConfig = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), LinksConfig.class);

        new LinksRunner(linksConfig, null).run();
    }
}
