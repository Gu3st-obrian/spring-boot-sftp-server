package com.example.demo.dao;

import com.example.demo.model.Container;
import com.example.demo.model.Folder;
import com.example.demo.model.Server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerDao {

    UUID createServer(UUID id, Server server);

    default UUID createServer(Server server) {
        UUID id = UUID.randomUUID();
        return createServer(id, server);
    }

    List<Server> getAllServer();

    Optional<Server> getServerById(UUID id);

    int deleteServer(UUID id);

    int updateServer(UUID id, Server server);

    List<Folder> directoryContent(Container container);

    int retrieveFile(Container container);
}
