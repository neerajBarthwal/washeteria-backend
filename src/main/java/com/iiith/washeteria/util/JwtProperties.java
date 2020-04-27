package com.iiith.washeteria.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;
@Component
public class JwtProperties {
	private static JwtProperties instance;
    private static Properties properties = new Properties();

    static {
        try {
            InputStream resourceAsStream = JwtProperties.class.getClassLoader().getResourceAsStream("jwt.properties");
            properties.load(resourceAsStream);
            System.out.println(properties.getProperty("jwt.issuer"));
            System.out.println(properties.getProperty("jwt.secret.key"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JwtProperties() {
    }

    public static JwtProperties getInstance() {
        if (instance == null) {
            instance = new JwtProperties();
        }

        return instance;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getJwtIssuer() {
        return properties.getProperty("jwt.issuer");
    }

    public String getJwtSecretKey() {
        return properties.getProperty("jwt.secret.key");
    }
}
