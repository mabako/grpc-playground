package io.github.mabako.playground.grpcserver;

import io.github.mabako.playground.proto.GreeterGrpc;
import io.github.mabako.playground.proto.HelloReply;
import io.github.mabako.playground.proto.HelloRequest;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService
public class GreetingService extends GreeterGrpc.GreeterImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(GreetingService.class);

  @Override
  public void sayHello(final HelloRequest request, final StreamObserver<HelloReply> responseObserver) {
    LOG.info("Received hello request from {}", request.getName());

    HelloReply response = HelloReply.newBuilder()
        .setMessage(String.format("Hello %s!", request.getName()))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
