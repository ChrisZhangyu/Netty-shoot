package com.chris.server;

import io.netty.channel.Channel;

public class ServerGetHTTPHandlerFactory extends ServerHandlerFactory{

    @Override
    public ServerGetHTTPHandler getHTTPHandler() {
        ServerGetHTTPHandler temp = new ServerGetHTTPHandler();
        temp.setServerSocketChannel(this.channel);
        return temp;
    }


    public void setChannel(Channel channel){
        this.channel = channel;
    }
}
