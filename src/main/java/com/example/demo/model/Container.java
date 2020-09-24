package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Container {

    private final UUID serverId;
    private final String path;

    public Container(@JsonProperty("server") UUID server,
                     @JsonProperty("path") String path) {
        this.serverId = server;
        this.path = path;
    }

    public UUID getServerId() {
        return this.serverId;
    }

    public String getPath() {
        return this.path;
    }
}
