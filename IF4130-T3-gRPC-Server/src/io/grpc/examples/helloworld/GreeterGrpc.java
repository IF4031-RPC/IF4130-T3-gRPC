package io.grpc.examples.helloworld;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@javax.annotation.Generated("by gRPC proto compiler")
public class GreeterGrpc {

  // Static method descriptors that strictly reflect the proto.
  public static final io.grpc.MethodDescriptor<io.grpc.examples.helloworld.GRPCRequest,
      io.grpc.examples.helloworld.GRPCResponse> METHOD_SAY_HELLO =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          "helloworld.Greeter", "SayHello",
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.helloworld.GRPCRequest.parser()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.helloworld.GRPCResponse.parser()));
  public static final io.grpc.MethodDescriptor<io.grpc.examples.helloworld.MessageRequest,
      io.grpc.examples.helloworld.GRPCResponse> METHOD_GET_MESSAGE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          "helloworld.Greeter", "GetMessage",
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.helloworld.MessageRequest.parser()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.helloworld.GRPCResponse.parser()));

  public static GreeterStub newStub(io.grpc.Channel channel) {
    return new GreeterStub(channel);
  }

  public static GreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GreeterBlockingStub(channel);
  }

  public static GreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GreeterFutureStub(channel);
  }

  public static interface Greeter {

    public void sayHello(io.grpc.examples.helloworld.GRPCRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver);

    public void getMessage(io.grpc.examples.helloworld.MessageRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver);
  }

  public static interface GreeterBlockingClient {

    public io.grpc.examples.helloworld.GRPCResponse sayHello(io.grpc.examples.helloworld.GRPCRequest request);

    public io.grpc.examples.helloworld.GRPCResponse getMessage(io.grpc.examples.helloworld.MessageRequest request);
  }

  public static interface GreeterFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.helloworld.GRPCResponse> sayHello(
        io.grpc.examples.helloworld.GRPCRequest request);

    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.helloworld.GRPCResponse> getMessage(
        io.grpc.examples.helloworld.MessageRequest request);
  }

  public static class GreeterStub extends io.grpc.stub.AbstractStub<GreeterStub>
      implements Greeter {
    private GreeterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterStub(channel, callOptions);
    }

    @java.lang.Override
    public void sayHello(io.grpc.examples.helloworld.GRPCRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver) {
      asyncUnaryCall(
          channel.newCall(METHOD_SAY_HELLO, callOptions), request, responseObserver);
    }

    @java.lang.Override
    public void getMessage(io.grpc.examples.helloworld.MessageRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver) {
      asyncUnaryCall(
          channel.newCall(METHOD_GET_MESSAGE, callOptions), request, responseObserver);
    }
  }

  public static class GreeterBlockingStub extends io.grpc.stub.AbstractStub<GreeterBlockingStub>
      implements GreeterBlockingClient {
    private GreeterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.examples.helloworld.GRPCResponse sayHello(io.grpc.examples.helloworld.GRPCRequest request) {
      return blockingUnaryCall(
          channel.newCall(METHOD_SAY_HELLO, callOptions), request);
    }

    @java.lang.Override
    public io.grpc.examples.helloworld.GRPCResponse getMessage(io.grpc.examples.helloworld.MessageRequest request) {
      return blockingUnaryCall(
          channel.newCall(METHOD_GET_MESSAGE, callOptions), request);
    }
  }

  public static class GreeterFutureStub extends io.grpc.stub.AbstractStub<GreeterFutureStub>
      implements GreeterFutureClient {
    private GreeterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.helloworld.GRPCResponse> sayHello(
        io.grpc.examples.helloworld.GRPCRequest request) {
      return futureUnaryCall(
          channel.newCall(METHOD_SAY_HELLO, callOptions), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.helloworld.GRPCResponse> getMessage(
        io.grpc.examples.helloworld.MessageRequest request) {
      return futureUnaryCall(
          channel.newCall(METHOD_GET_MESSAGE, callOptions), request);
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final Greeter serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder("helloworld.Greeter")
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_SAY_HELLO,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                io.grpc.examples.helloworld.GRPCRequest,
                io.grpc.examples.helloworld.GRPCResponse>() {
              @java.lang.Override
              public void invoke(
                  io.grpc.examples.helloworld.GRPCRequest request,
                  io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver) {
                serviceImpl.sayHello(request, responseObserver);
              }
            })))
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_GET_MESSAGE,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                io.grpc.examples.helloworld.MessageRequest,
                io.grpc.examples.helloworld.GRPCResponse>() {
              @java.lang.Override
              public void invoke(
                  io.grpc.examples.helloworld.MessageRequest request,
                  io.grpc.stub.StreamObserver<io.grpc.examples.helloworld.GRPCResponse> responseObserver) {
                serviceImpl.getMessage(request, responseObserver);
              }
            }))).build();
  }
}
