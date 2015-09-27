/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.grpc.examples.helloworld;

/**
 *
 * @author Imballinst
 */
public class ChannelLastMsg {
    public String channel;
    public int lastID;
    
    public ChannelLastMsg(int lastID_, String channel_) {
        channel = channel_;
        lastID = lastID_;
    }
}
