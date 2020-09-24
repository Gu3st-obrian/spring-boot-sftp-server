package com.example.demo.service;

import com.example.demo.dao.ServerDao;
import com.example.demo.model.Container;
import com.example.demo.model.Folder;
import com.example.demo.model.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServerService {

    private final ServerDao serverDao;

    @Autowired
    public ServerService(@Qualifier("tempDB") ServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public UUID createServer(Server server) {
        return this.serverDao.createServer(server);
    }

    public List<Server> getAllServer() {
        return this.serverDao.getAllServer();
    }

    public Optional<Server> getServerById(UUID id) {
        return this.serverDao.getServerById(id);
    }

    public int deleteServer(UUID id) {
        return this.serverDao.deleteServer(id);
    }

    public int updateServer(UUID id, Server server) {
        return this.serverDao.updateServer(id, server);
    }

    public List<Folder> directoryContent(Container container) {
        return this.serverDao.directoryContent(container);
    }

    public int retrieveFile(Container container) {
        return this.serverDao.retrieveFile(container);
    }
}
