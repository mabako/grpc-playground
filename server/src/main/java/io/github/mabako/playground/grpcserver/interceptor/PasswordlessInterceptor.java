package io.github.mabako.playground.grpcserver.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

/**
 * Just pretend a user is all that we need.
 *
 * <p>
 * Since we don't perform actual authentication/authorization, this isn't sent as 'Authorization' header.
 * </p>
 */
public class PasswordlessInterceptor implements ServerInterceptor {

  @SuppressWarnings("rawtypes")
  private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {
  };

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall, final Metadata metadata, final ServerCallHandler<ReqT, RespT> serverCallHandler) {
    byte[] encodedUser = metadata.get(Constants.USER_METADATA_KEY);
    if (encodedUser == null || encodedUser.length == 0) {
      serverCall.close(Status.UNAUTHENTICATED.withDescription("No user provided"), metadata);

      //noinspection unchecked
      return NOOP_LISTENER;
    }

    String user = new String(encodedUser, StandardCharsets.UTF_8);
    Context ctx = Context.current().withValue(Constants.USER_CTX_KEY, user);

    return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
  }
}
