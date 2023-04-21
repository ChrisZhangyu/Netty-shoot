package com.chris.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class ServerHandlerFactory extends ChannelInboundHandlerAdapter {
    public ChannelContainer channelContainer;
    public abstract ChannelHandler getHTTPHandler();

}
