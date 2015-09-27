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

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import io.grpc.ServerImpl;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.Random;

import java.util.logging.Logger;
import org.bson.Document;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
  
  /* Db Config */
    MongoClient mongoClient = new MongoClient("localhost");
    MongoDatabase database = mongoClient.getDatabase("chatRPC");
  
    /* The port on which the server should run */
    private int port = 50051;
    private ServerImpl server;

    private void start() throws Exception {
      server = NettyServerBuilder.forPort(port)
          .addService(GreeterGrpc.bindService(new GreeterImpl()))
          .build().start();
      logger.info("Server started, listening on " + port);
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          // Use stderr here since the logger may have been reset by its JVM shutdown hook.
          System.err.println("*** shutting down gRPC server since JVM is shutting down");
          Server.this.stop();
          System.err.println("*** server shut down");
        }
      });
    }

    private void stop() {
      if (server != null) {
        server.shutdown();
      }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws Exception {
      final Server server = new Server();
      server.start();
    }

    private class GreeterImpl implements GreeterGrpc.Greeter {

      @Override
      public void sayHello(GRPCRequest req, StreamObserver<GRPCResponse> responseObserver) {
          GRPCResponse reply = GRPCResponse.newBuilder().setMessage("Hello " + req.getToken() + req.getMessage()).build();
          responseObserver.onValue(reply);
          responseObserver.onCompleted();
      }

      public String regNick(String token, String nick) {
          if (token.equals("") && nick != null) {
              if (isNickExist(nick)) {
                  //nick exists
                  return "Nick exists!";
              }
              else {
                  //if nick doesn't exist
                  nick = saveNick(nick);
                  return nick;
              }
          }
          else if (token.equals("") && nick == null) {
              String newNick = "";
              do {
                  newNick = randomNick();
              } while (isNickExist(newNick));
              //save nick
              saveNick(nick);
              return newNick;
          }
          else {
              //already registered
              return "Nick already registered.";
          }
      }

      public String joinChannel(String token, String channel) {
          /* belum di cek apakah udah join */
          /* cek channel exist */
          if(this.isChannelExist(channel))
          {
              /* subscribe */
              this.subscribeChannel(token, channel);
              return "Channel subscribed.";
          }
          else
          {
              // create channel 
              this.createChannel(channel);

              /* subscribe */
              this.subscribeChannel(token, channel);
              return "Channel created and subscribed.";
          }
      }

      private boolean subscribeChannel(String token, String channel)
      {
          MongoCollection<Document> userCollection = database.getCollection("User");
          Document channelDoc = new Document("name", channel);
          Document listChannel = new Document("channels", channel);
          userCollection.updateOne(eq("nick", token), new Document("$push", listChannel));
          //userCollection.updateOne(eq("nick", token), new Document("$set",new Document("channel", channel)));
          return true;
      }

      public String leaveChannel(String token, String channel) {
  //        if (isChannelSubscribed(token,channel)) {
              //if channel is subscribed
              //leave channel
              return this.deleteMember(token, channel);
  //        }
  //        else {
              //if channel isn't subcribed
  //            return "You aren't subscribed to that channel.";

  //        }
      }

      public boolean saveMessage(String token, String channel, String message) {
          return this.saveToDB(token, channel, message);
      }

      public boolean saveToDB(String token, String channel, String message) {
          MongoCollection<Document> messageCollection = database.getCollection(channel+"Message");
          int lastId;
  //        System.out.println(this.getLastMessageId(channel));
  //        Document listMessage = new Document("messages",new Document("id", 3).append("nick", token).append("messsage", message));
  //        Document updateQuery = new Document("$push", listMessage);
  //        Document findQuery = new Document("name", channel);
  //        channelCollection.updateOne(findQuery, updateQuery);

  //        Document messageDoc = new Document("id", this.getLastMessageId(channel)).append("nick", token).append("message", message);
  //        messageCollection.insertOne(messageDoc);

          try
          {
              lastId = this.getLastMessageId(channel);
              lastId++;
          }
          catch(Exception e){
              lastId = 1;
          }
          Document doc = new Document("id", lastId)
                          .append("nick", token)
                          .append("message", message);
          messageCollection.insertOne(doc);
          return true;
      }

      private int getLastMessageId(String channel)
      {
          MongoCollection<Document> collection = database.getCollection(channel+"Message");
          Document myDoc = collection.find().sort(descending("id")).first();
          return myDoc.getInteger("id");
      }

      public String getMessage(List<ChannelLastMsg> clm, String token) {
          String messages = "";
          for(ChannelLastMsg channelStruct : clm)
          {
              MongoCollection<Document> messageCollection = database.getCollection(channelStruct.channel+"Message");
              FindIterable<Document> iterable = messageCollection.find(new Document("id", new Document("$gt", channelStruct.lastID)));
              for(Document doc : iterable)
              {
  //                if(doc.getString("nick").equals(token))
                      messages = messages + channelStruct.channel+":@" + token + ' ' + doc.getString("message") + '\n';
  //                    System.out.println(doc.getString("message"));
              }
          }   
          return messages;
      }

      public String iSend(String token, String message) {
          String response = "";
          try {
              String[] command = message.split(" ", 2);
              switch (command[0]) {
                  case "/NICK": 
                      response = regNick(token, command[1]);
                      break;
                  case "/JOIN": 
                      response = joinChannel(token, command[1]);
                      break;
                  case "/LEAVE": 
                      response = leaveChannel(token, command[1]);
                      break;
                  default:
                      //send message to a channel
                      if (command[0].charAt(0) == '@') {
                          String channelName = command[0].substring(1, command[0].length());
                          //correct
                          saveMessage(token, channelName, command[1]);
                          response = "Success sending message to the channel!";
                      }
                      else {
                          //false
                          response = "Failed sending message to the channel.";
                      }
                      break;
              }
          } catch (Exception e) {
              if (message.compareTo("/NICK") == 0) {
                  //random nick
                  response = regNick(token, randomNick());
              }
              else if ((message.compareTo("/JOIN") == 0) || (message.compareTo("/LEAVE") == 0)) {
                  //error
                  response = "Please enter channel name!";
              }
              else if (message.charAt(0) == '@') {
                  response = "Please enter your message for the channel.";
              }
              else {
                  response = "Invalid command.";
              }
          }
          return response;
      }

      public boolean isNickExist(String nick) {
          MongoCollection<Document> userCollection = database.getCollection("User");
          return userCollection.find(eq("nick", nick)).first() != null;
      }

      public boolean isChannelExist(String channel) {
          MongoCollection<Document> channelCollection = database.getCollection("Channel");
          return channelCollection.find(eq("name", channel)).first() != null;
      }

      public String saveNick(String nick) {
          MongoCollection<Document> userCollection = database.getCollection("User");
          Document doc = new Document("nick", nick);
          userCollection.insertOne(doc);
          return nick;
      }

      public String randomNick() {
          String nick = "";
          String[] pool = {"Zacky", "Raddy", "Will", "Ohm", "Ary", "Ardee", "Ilma", "Khidr", "Galang", "Theo", "Tereta", "Rossi", 
              "Ivina", "Nicy", "Kiito"};
          Random randomGenerator = new Random();
          int randomInt = randomGenerator.nextInt(100);
          int randomNick = randomGenerator.nextInt(15);

          nick = pool[randomNick].concat(Integer.toString(randomInt));
          return nick;
      }

      public String createChannel(String channel) {
          MongoCollection<Document> channelCollection = database.getCollection("Channel");

              /* channel not exist */
              Document doc = new Document("name", channel);

              channelCollection.insertOne(doc);

              return "Channel berhasil dibuat";
      }

      public String deleteMember(String token, String channel) {
          MongoCollection<Document> userCollection = database.getCollection("User");
          Document match = new Document("nick", token);
          Document remove = new Document("channels", channel);
          userCollection.updateOne(match, new Document("$pull", remove));
  //        userCollection.updateOne(eq("nick",token), new Document("$pull", remove));
          return "Channel unsubscribed.";
      }

      public boolean isChannelSubscribed(String token, String channel) {
  //        MongoCollection<Document> userCollection = database.getCollection("User");
  //        FindIterable<Document> cursor = userCollection.find(eq("nick",token));
  //        System.out.println(cursor.);
  //        for(Document doc : userCollection.find(eq("nick",token)))
  //        {
  //            System.out.println(doc);
  //        }
  //        for(String r : userCollection.get)
  //        return true;
          throw new UnsupportedOperationException("Not supported yet.");
      }
    }
}
