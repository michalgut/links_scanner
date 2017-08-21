package com.jamfnow.qa.links;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MappingTest {

    @Test
    public void verifyObjectCanBeCreated() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test.yml").getFile());
        LinksConfig linksConfig = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), LinksConfig.class);

        assertEquals(linksConfig.getBrowser(), Browser.chrome);
    }

    @Test
    public void verifyObjectCanBeCreated2() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("test_no_login.yml").getFile());
        LinksConfig linksConfig = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), LinksConfig.class);

        assertNull(linksConfig.getLogin());
    }
}
