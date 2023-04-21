package com.chris.server;

import io.netty.channel.Channel;

import java.lang.reflect.Method;

public class ServerGetHTTPHandlerFactory extends ServerHandlerFactory{
    @Override
    public ServerGetHTTPHandler getHTTPHandler() {
        ServerGetHTTPHandler temp = new ServerGetHTTPHandler();
        temp.setContainer(this.channelContainer);
        return temp;
    }

    public ServerGetHTTPHandlerFactory() {
    }

    public ServerGetHTTPHandlerFactory(ChannelContainer channelContainer) {
        this.channelContainer = channelContainer;
    }

    public void setChannelContainer(ChannelContainer channelContainer){
        this.channelContainer = channelContainer;
    }
}
