package com.chris.server;

import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.concurrent.GlobalEventExecutor;

public  class ServerProviderHandler extends ChannelInboundHandlerAdapter {

    private Channel responseChannel;

    public Channel getResponseChannel() {
        return responseChannel;
    }

    public void setResponseChannel(Channel responseChannel) {
        this.responseChannel = responseChannel;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);

    }




}
