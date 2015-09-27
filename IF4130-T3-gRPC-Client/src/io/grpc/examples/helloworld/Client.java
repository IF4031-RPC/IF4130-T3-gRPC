/*
 * Copyright 2015, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 *    * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.grpc.examples.helloworld;

import io.grpc.ChannelImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    
    public static String token = "";
    public static ArrayList<ChannelLastMsg> list = new ArrayList<>();
    
    private final ChannelImpl channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public Client(String host, int port) {
      channel =
          NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
              .build();
      blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public String greet(String name, String message) {
      try {
        logger.info("Will try to greet " + name + " ...");
        GRPCRequest request = GRPCRequest.newBuilder()
                                  .setToken(name)
                                  .setMessage(message)
                                  .build();
        GRPCResponse response = blockingStub.sayHello(request);
        logger.info("Greeting: " + response.getMessage());
        return "halo";
      } catch (RuntimeException e) {
        logger.log(Level.WARNING, "RPC failed", e);
        return "fail";
      }
    }
    
    public String greet(ArrayList<ChannelLastMsg> clm, String token) {
      try {
        MessageRequest request = MessageRequest.newBuilder()
                                  .setToken(token)
                                  .build();
        for (int i = 0; i < clm.size(); i++) {
            request.toBuilder().setClm(i, clm.get(i)).build();
        }
        GRPCResponse response = blockingStub.getMessage(request);
        //logger.info("Greeting: " + response.getMessage());
        return "halo";
      } catch (RuntimeException e) {
        logger.log(Level.WARNING, "RPC failed2", e);
        return "fail";
      }
    }
    
    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception {
        new Thread(new ReadRunnable()).start();
        new Thread(new PrintRunnable()).start();
    }
}

 class ReadRunnable implements Runnable {
    @Override
    public void run() {
        Client client = new Client("localhost", 50051);
        try {
          /* Access a service running on the local machine on port 50051 */
//          String user = "world";
//          
//          client.greet(user, "dummy");
            perform(client);
        } finally {
            try {
                client.shutdown();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReadRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void perform(Client client) 
    {
        String uuid = null;
        while (true) {
            Scanner in = new Scanner(System.in);
            
            System.out.print("Please enter your command: ");
            String command = in.nextLine();
            
            String[] com = command.split(" ", 2);
            switch (com[0]) {
                case "/NICK": 
                    client.token = client.greet(client.token, command);
                    System.out.println(client.token);
                    break;
                case "/JOIN": 
                    System.out.println(client.greet(client.token, command));
                    ChannelLastMsg clm = ChannelLastMsg.newBuilder().
                                                                setChannel(com[1]).
                                                                setLastId(0).build();
                    if (client.list.add(clm)) {
                        System.out.println("Sukses nambahin channel ke client");
                        System.out.println(client.list.get(0).getChannel());
                    }
                    
                    break;
                case "/LEAVE": 
                    System.out.println(client.greet(client.token, command));
                    delElement(client.list, com[1]);
                    break;
                case "/EXIT":
                    System.exit(0);
                default:
                    //send message to a channel
                    System.out.println(client.greet(client.token, command));
                    break;
            }
        }
    }
    
    private static void delElement(List<ChannelLastMsg> clms, String channel)
    {
        Iterator<ChannelLastMsg> itr = clms.iterator();
        while(itr.hasNext())
        {
            ChannelLastMsg clm = (ChannelLastMsg)itr.next();
            if(clm.getChannel().equals(channel))
            {
                itr.remove();
                break;
            }
        }
    }
}

class PrintRunnable implements Runnable {
    @Override
    public void run() {
        Client client = new Client("localhost", 50051);
        try {
          /* Access a service running on the local machine on port 50051 */
//          String user = "world";
//          
//          client.greet(user, "dummy");
            perform(client);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrintRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                client.shutdown();
            } catch (InterruptedException ex) {
                Logger.getLogger(ReadRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void perform(Client client) throws InterruptedException
    {
        while (true) {
            try{
                String a = client.greet(client.list, client.token); //getMessage
                if(!a.equals(""))
                {
                    String [] messages = a.split("\n");
                    for(String message : messages)
                    {
                        int channelIndex = message.indexOf(":");
                        String channelName = message.substring(0, channelIndex);
                        lastIDIncrement(client.list, channelName);
                    }
                    System.out.println(a);
                }
            }
            catch(Exception e)
            {
                
            }
            Thread.sleep(500);
        }
    }
    private static void lastIDIncrement(ArrayList<ChannelLastMsg> list, String channel) {
        Iterator<ChannelLastMsg> itr = list.iterator();
        while(itr.hasNext())
        {
            ChannelLastMsg clm = (ChannelLastMsg)itr.next();
            if(clm.getChannel().equals(channel))
            {
                clm.toBuilder().setLastId(clm.getLastId() + 1);
                break;
            }
        }
    }
}