package com.chris.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chris
 * @date 2023/4/13 22:47
 */
public class ChannelContainer {
    private ConcurrentHashMap<Channel, LinkedList<Channel>> router;
    private ConcurrentHashMap<Channel, Channel> serverChannels;
    private ConcurrentHashMap<Channel, Channel> requestChannels;

    public ChannelContainer() {
        this.router = new ConcurrentHashMap<Channel, LinkedList<Channel>>();
        this.serverChannels = new ConcurrentHashMap<>();
        this.requestChannels = new ConcurrentHashMap<>();
    }

    public void registry(Channel channel, ChannelType channelType){
        if (channelType == ChannelType.Server){
            serverChannels.put(channel, channel);
            router.put(channel, new LinkedList<>());
        } else if (channelType == ChannelType.Request){
            requestChannels.put(channel, channel);

        }else {
            System.out.println("Channel不符合规范");
        }
    }

    public void remove(Channel channel, ChannelType channelType){
        if (channelType == ChannelType.Server){
            if (router.contains(channel)){
                router.remove(channel);
            }
            serverChannels.remove(channel, channel);
        } else if (channelType == ChannelType.Request){
            requestChannels.remove(channel, channel);
        }else {
            System.out.println("Channel不符合规范");
        }
    }
// 先实现单例模式
   public Channel getServerChannel(Channel requestChannel){
        Channel serverChannel = null;
        if (!requestChannels.contains(requestChannel)){
            requestChannels.put(requestChannel, requestChannel);
        }
        if (!serverChannels.isEmpty()){
            for (Channel channel : serverChannels.keySet()){
                this.setRoute(channel, requestChannel);
                return channel;
            }
        }else {
            System.out.println("服务端channel为空，检查服务端是否连接");
            return null;
        }
       return serverChannel;
   }

    private void setRoute(Channel server, Channel request){
        router.get(server).addLast(request);
    }

    public Channel getRequestChannel(Channel serverChannel){
        if (router.get(serverChannel).isEmpty()){
            System.out.println("当前没有请求需要返回响应");
            return null;
        }
       return router.get(serverChannel).removeFirst();
    }

}
