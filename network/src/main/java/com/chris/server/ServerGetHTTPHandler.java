package com.chris.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupException;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerGetHTTPHandler extends ChannelInboundHandlerAdapter {
    public Channel getServerSocketChannel() {
        return serverChannel;
    }

    public void setServerSocketChannel(Channel serverSocketChannel) {
        this.serverChannel = serverSocketChannel;
    }

    private Channel serverChannel;


    public ServerGetHTTPHandler() {
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object httpRequest) throws Exception {
        System.out.println("来自" + ctx.channel().remoteAddress() + "的请求");

        System.out.println("向通道" + serverChannel.id() + "发送请求");

        serverChannel.writeAndFlush(httpRequest).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.cause() != null){
                    System.out.println(future.cause());
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private class TransferRequestHandler extends ChannelInboundHandlerAdapter{
        private final Channel fatherChannel;

        private TransferRequestHandler(Channel fatherChannel) {
            this.fatherChannel = fatherChannel;
        }

        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object fullHttpResponse) throws Exception {
            System.out.println("传递消息");
            fatherChannel.writeAndFlush((FullHttpResponse) fullHttpResponse);
        }
    }


}
