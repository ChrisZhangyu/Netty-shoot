package com.chris.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public class ServerProviderHandlerFactory extends ServerHandlerFactory{
    @Override
    public ChannelHandler getHTTPHandler() {
        ServerProviderHandler temp = new ServerProviderHandler();
        temp.setResponseChannel(this.channel);
        return temp;
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }
}
