package io.github.mabako.playground.grpcserver;

import io.github.mabako.playground.grpcserver.interceptor.Constants;
import io.github.mabako.playground.grpcserver.interceptor.PasswordlessInterceptor;
import io.github.mabako.playground.proto.GreeterGrpc;
import io.github.mabako.playground.proto.HelloReply;
import io.github.mabako.playground.proto.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService(interceptors = PasswordlessInterceptor.class)
public class GreetingService extends GreeterGrpc.GreeterImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(GreetingService.class);

  @Override
  public void sayHello(final HelloRequest request, final StreamObserver<HelloReply> responseObserver) {
    LOG.info("User as per metadata: {}", Constants.USER_CTX_KEY.get());
    LOG.info("Received hello request from {}", request.getName());

    HelloReply response = HelloReply.newBuilder()
        .setMessage(String.format("Hello %s!", request.getName()))
        .setCtxUser(Constants.USER_CTX_KEY.get())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
