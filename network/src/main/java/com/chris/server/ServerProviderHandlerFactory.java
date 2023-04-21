package com.chris.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

public class ServerProviderHandlerFactory extends ServerHandlerFactory{
    @Override
    public ChannelHandler getHTTPHandler() {
        ServerProviderHandler temp = new ServerProviderHandler();
        temp.setContainer(this.channelContainer);

        return temp;
    }

    public ServerProviderHandlerFactory() {
    }

    public ServerProviderHandlerFactory(ChannelContainer channelContainer) {
        this.channelContainer = channelContainer;
    }

    public void setChannelContainer(ChannelContainer channelContainer){
        this.channelContainer = channelContainer;
    }
}
