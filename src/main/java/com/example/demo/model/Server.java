package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Server {

    private final UUID id;
    private final String hostname;
    private final int port;
    private final String username;
    private final String password;

    public Server(@JsonProperty("id") UUID id,
                  @JsonProperty("host") String host,
                  @JsonProperty("port") int port,
                  @JsonProperty("user") String user,
                  @JsonProperty("pass") String pass) {
        this.id = id;
        this.hostname = host;
        this.port = port;
        this.username = user;
        this.password = pass;
    }

    public UUID getId() {
        return this.id;
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
