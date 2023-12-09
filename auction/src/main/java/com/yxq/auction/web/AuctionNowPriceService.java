package com.yxq.auction.web;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/newprice/{aid}")
public class AuctionNowPriceService {

 //   private Session session;

   // private Integer aid;
    public static ConcurrentHashMap<Integer, List<Session>> mymap = new ConcurrentHashMap<>();
    @OnOpen
    public void openSocket(@PathParam("aid") Integer aid,Session session) throws IOException {
        //System.out.println("得到客户端的连接"+session.getId());
        //session.getBasicRemote().sendText("from:service===1");
        List<Session> list = null;
        if(!mymap.containsKey(aid)){
            list = new ArrayList<>();
            list.add(session);
            mymap.put(aid,list);
        }else {
            list = mymap.get(aid);
            list.add(session);
        }
        Set<Integer> set = mymap.keySet();
        Iterator<Integer> it = set.iterator();
        while(it.hasNext()){
            Integer key = it.next();
            List<Session> vallist = mymap.get(key);
        }


    }

    /*@OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("得到客户端的消息"+message);
        session.getBasicRemote().sendText("from:service===2");
    }*/

    @OnClose
    public void closeSocket(Session session){
        Iterator<Integer> it = mymap.keySet().iterator();
        while (it.hasNext()){
            Integer key = it.next();
            List<Session> vallist = mymap.get(key);
            vallist.remove(session);
        }
    }
    public static void sendMessage(int aid,String vid,int nprice) throws IOException {
        List<Session> list = mymap.get(aid);

        if(list != null){
            for(Session session : list){

                session.getBasicRemote().sendText(nprice+"");
                System.out.println(nprice);
            }
        }


    }


}
