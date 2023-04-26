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
    private ChannelContainer container;

    public void setContainer(ChannelContainer container) {
        this.container = container;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        container.registry(ctx.channel(), ChannelType.Server);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        container.remove(ctx.channel(), ChannelType.Server);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
            Channel requestChannel = container.getRequestChannel(ctx.channel());
            System.out.println("已经调用服务得到返回的数据,向请求端channel" + requestChannel + "写回数据");

            requestChannel.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.print("返回数据时出错");
    }
}
