package io.github.mabako.playground.grpcserver.interceptor;

import static io.grpc.Metadata.BINARY_BYTE_MARSHALLER;

import io.grpc.Context;
import io.grpc.Metadata;

public final class Constants {
  public static final Context.Key<String> USER_CTX_KEY = Context.key("User");
  public static final Metadata.Key<byte[]> USER_METADATA_KEY = Metadata.Key.of("X-User-bin", BINARY_BYTE_MARSHALLER);
}
