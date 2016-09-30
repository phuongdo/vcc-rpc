package com.orbekk.example;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.orbekk.example.CTR;
import com.orbekk.protobuf.SimpleProtobufServer;

import java.util.Random;

/**
 * Created by pdoviet on 9/30/16.
 */
public class CTRServer {


    public class CTRPredictionService extends CTR.CTRPredictionService {
        Random random = new Random();

        @Override
        public void getPrediction(RpcController controller, CTR.Request request,
                                  RpcCallback<CTR.Response> done) {
//            String fortune = fortunes[random.nextInt(fortunes.length)];

            long sum = 0;
            for (int i = 1; i < 100000; i++) {
                for (int j = 1; j < 100000; j++) {
                    sum = i + j;
                }
            }



            float pred = random.nextInt(10000) / sum;
            int bannerid = request.getBannerId();
            CTR.Response.Builder reply =
                    CTR.Response.newBuilder().
                            setBannerId(bannerid).setCtrPrediction(pred);
            done.run(reply.build());
        }
    }

    public void runServer(int port) {
        SimpleProtobufServer server = SimpleProtobufServer.create(port);
        server.registerService(new CTRPredictionService());
        server.start();
        System.out.println("Running server on port " + server.getPort());
        try {
            server.join();
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        int port = 9999;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new CTRServer().runServer(port);
    }
}
