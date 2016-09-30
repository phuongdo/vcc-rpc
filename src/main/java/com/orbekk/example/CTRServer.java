package com.orbekk.example;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.socketrpc.RpcServer;
import com.googlecode.protobuf.socketrpc.ServerRpcConnectionFactory;
import com.googlecode.protobuf.socketrpc.SocketRpcConnectionFactories;
import com.orbekk.example.CTR;
import com.orbekk.protobuf.SimpleProtobufServer;

import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Created by pdoviet on 9/30/16.
 */
public class CTRServer {


    public class MyBlockingInterfaceImpl implements CTR.CTRPredictionService.BlockingInterface {
        Random random = new Random();
//
//        @Override
//        public void getPrediction(RpcController controller, CTR.Request request,
//                                  RpcCallback<CTR.Response> done) {
////            String fortune = fortunes[random.nextInt(fortunes.length)];
//
//            long sum = 0;
//            for (int i = 1; i < 100000; i++) {
//                for (int j = 1; j < 100000; j++) {
//                    sum = i + j;
//                }
//            }
//
//
//            float pred = (float) random.nextInt(10000) / sum;
//            int bannerid = request.getBannerId();
//            CTR.Response.Builder reply =
//                    CTR.Response.newBuilder().
//                            setBannerId(bannerid).setCtrPrediction(pred);
//            done.run(reply.build());
//        }

        @Override
        public CTR.Response getPrediction(RpcController controller, CTR.Request request) throws ServiceException {

            long sum = 0;
            for (int i = 1; i < 100000; i++) {
                sum += i;
            }


            float pred = (float) random.nextInt(10000) / sum;
            int bannerid = request.getBannerId();
            CTR.Response.Builder reply =
                    CTR.Response.newBuilder().
                            setBannerId(bannerid).setCtrPrediction(pred);

            return reply.build();
        }
    }

    public void runServer(int port) {
//        SimpleProtobufServer server = SimpleProtobufServer.create(port);
//        server.registerService(new CTRPredictionService());
//        server.start();
//        System.out.println("Running server on port " + server.getPort());
//        try {
//            server.join();
//        } catch (InterruptedException e) {
//        }

        int threadPoolSize = 20;
        // Start server
        ServerRpcConnectionFactory rpcConnectionFactory = SocketRpcConnectionFactories
                .createServerRpcConnectionFactory(9999);
        RpcServer server = new RpcServer(rpcConnectionFactory,
                Executors.newFixedThreadPool(threadPoolSize), true);
//        server.registerService(new CTRPredictionService()); // For non-blocking impl
        server.registerBlockingService(CTR.CTRPredictionService
                .newReflectiveBlockingService(new MyBlockingInterfaceImpl())); // For blocking impl
        server.run();

    }

    public static void main(String[] args) {
        int port = 9999;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new CTRServer().runServer(port);
    }
}
