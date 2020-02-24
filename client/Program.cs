using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Grpc.Core;
using Grpc.Net.Client;
using GrpcClient.Proto;

namespace GrpcClient
{
    static class Program
    {
        static async Task Main(string[] args)
        {
            var httpClientHandler = new HttpClientHandler
            {
                ServerCertificateCustomValidationCallback =
                    (message, certificate, chain, sslPolicyErrors) =>
                        certificate.Thumbprint == "E605A7138457ADB7D4C2E770F1ED585D344F1940" &&
                        certificate.NotBefore < DateTime.Now &&
                        certificate.NotAfter > DateTime.Now
            };
            using var httpClient = new HttpClient(httpClientHandler);

            using var channel = GrpcChannel.ForAddress("https://localhost:5001", new GrpcChannelOptions
            {
                HttpClient = httpClient,
            });

            var metadata = new Metadata
            {
                {"X-User-bin", Encoding.UTF8.GetBytes("äöüß\\'")}
            };

            var client = new Greeter.GreeterClient(channel);
            var reply = await client.SayHelloAsync(new HelloRequest {Name = "not me"}, metadata);

            Console.WriteLine($"{reply.Message} (actual user is {reply.CtxUser})");
        }
    }
}
