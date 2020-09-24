package com.example.demo.api;

import com.example.demo.model.Container;
import com.example.demo.model.Folder;
import com.example.demo.model.Server;
import com.example.demo.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/server")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServerController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ServerService serverService;

    @Autowired
    public ServerController(ServerService service) {
        this.serverService = service;
    }

    @PostMapping
    public UUID addServer(@RequestBody Server server) {
        return this.serverService.createServer(server);
    }

    @GetMapping
    public List<Server> getAllServer() {
        // Get list of servers save in local Array dbo.
        return this.serverService.getAllServer();
    }

    @GetMapping(path = "{id}")
    public Server getServerById(@PathVariable("id") UUID id) {
        // Get information about one server.
        return this.serverService.getServerById(id)
                .orElse(null);
    }

    @PutMapping(path = "{id}")
    public void updateServer(@PathVariable("id") UUID id, @RequestBody Server server) {
        // Update server configuration.
        this.serverService.updateServer(id, server);
    }

    @DeleteMapping(path = "{id}")
    public void deleteServer(@PathVariable("id") UUID id) {
        // Remove server from local Array dbo.
        this.serverService.deleteServer(id);
    }

    @GetMapping("file/list")
    public List<Folder> directoryContent(@RequestBody Container container) {
        // Get list of all files contained in the given folder.
        return this.serverService.directoryContent(container);
    }

    @GetMapping("file/retrieve")
    public int retrieveFile(@RequestBody Container container) {
        // Get file from server and return 1 if success and 0 if not.
        // File are saving in static folder under resources.
        return this.serverService.retrieveFile(container);
    }
}
