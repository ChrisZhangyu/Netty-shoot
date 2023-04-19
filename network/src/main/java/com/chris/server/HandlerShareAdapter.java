package com.chris.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chris
 * @date 2023/4/13 22:47
 */
public class HandlerShareAdapter  {
    private ConcurrentHashMap<Integer, List<Integer>> router;
    private HashMap<Integer, Channel> table;

    public HandlerShareAdapter() {
        this.router = new ConcurrentHashMap<Integer, List<Integer>>();
        this.table = new HashMap<>();
    }

    public void registry(int channelId, Channel channel){
        table.put(channelId, channel);
    }

    public void remove(int channelId, Channel channel){
        table.remove(channelId);
    }

    public void send(int channelId, Object msg){
        List<Integer> targetChannel = router.get(channelId);
        for (int item : targetChannel){
            table.get(item).writeAndFlush(msg);
        }
    }

    public void route(int source, int target){
        router.getOrDefault(source, new ArrayList<>()).add(target);
    }



}
