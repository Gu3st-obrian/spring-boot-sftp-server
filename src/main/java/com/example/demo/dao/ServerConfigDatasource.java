package com.example.demo.dao;

import com.example.demo.model.Container;
import com.example.demo.model.Folder;
import com.example.demo.model.Server;
import com.jcraft.jsch.*;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.jcraft.jsch.ChannelSftp.LsEntrySelector;


@Repository("tempDB")
public class ServerConfigDatasource implements ServerDao {

    private final List<Server> dbo = new ArrayList<>();
    private Session session = null;
    private Channel channel = null;

    @Override
    public UUID createServer(UUID id, Server server) {
        try {
            this.dbo.add(new Server(id,
                    server.getHostname(),
                    server.getPort(),
                    server.getUsername(),
                    server.getPassword()
            ));
            return id;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Server> getAllServer() {
        return this.dbo;
    }

    @Override
    public Optional<Server> getServerById(UUID id) {
        return this.dbo.stream()
                .filter(server -> server.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteServer(UUID id) {
        Optional<Server> serverMaybe = this.getServerById(id);
        if (serverMaybe.isEmpty()) {
            return 0;
        }
        this.dbo.remove(serverMaybe.get());
        return 1;
    }

    @Override
    public int updateServer(UUID id, Server newServer) {
        return this.getServerById(id)
                .map(oldServer -> {
                    int indexOfServerToUpdate = this.dbo.indexOf(oldServer);
                    if (indexOfServerToUpdate >= 0) {
                        this.dbo.set(indexOfServerToUpdate, new Server(id,
                                newServer.getHostname(),
                                newServer.getPort(),
                                newServer.getUsername(),
                                newServer.getPassword()
                        ));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

    private ChannelSftp getChannel(Container container) {
        Server serverInfo = this.getServerById(container.getServerId())
                .orElse(null);
        if (serverInfo == null) {
            return null;
        }

        try {
            JSch plugin = new JSch();
            this.session = plugin.getSession(
                    serverInfo.getUsername(),
                    serverInfo.getHostname(),
                    serverInfo.getPort()
            );
            this.session .setPassword(serverInfo.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            this.session.setConfig(config);

            this.session.connect();
            this.channel = this.session.openChannel("sftp");
            this.channel.connect();
            return (ChannelSftp)this.channel;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Folder> directoryContent(Container container) {
        ChannelSftp channelSftp = this.getChannel(container);
        List<Folder> fileList = new ArrayList<>();

        if (channelSftp == null) {
            return fileList;
        }

        try {
            LsEntrySelector selector = entry -> {
                final String filename = entry.getFilename();
                if (filename.equals(".") || filename.equals("..")) {
                    return LsEntrySelector.CONTINUE;
                }
                String permission = entry.getAttrs().getPermissionsString();
                if (entry.getAttrs().isLink()) {
                    fileList.add(new Folder(filename, permission, "LINK"));
                }
                else if (entry.getAttrs().isDir()) {
                    fileList.add(new Folder(filename, permission, "DIR"));
                }
                else {
                    fileList.add(new Folder(filename, permission, "FILE"));
                }
                return LsEntrySelector.CONTINUE;
            };

            channelSftp.ls(container.getPath(), selector);
            channelSftp.disconnect();

            return fileList;
        }
        catch (SftpException ex) {
            ex.printStackTrace();
            return fileList;
        }
        finally {
            if(this.session != null) this.session.disconnect();
            if(this.channel != null) this.channel.disconnect();
        }
    }

    @Override
    public int retrieveFile(Container container) {
        ChannelSftp channelSftp = this.getChannel(container);
        if (channelSftp == null) {
            return 0;
        }
        try {
            channelSftp.get(container.getPath(), "/static/" + container.getPath());
            return 1;
        } catch (SftpException ex) {
            ex.printStackTrace();
            return 0;
        }
        finally {
            if(this.session != null) this.session.disconnect();
            if(this.channel != null) this.channel.disconnect();
        }
    }
}
