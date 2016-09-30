package com.orbekk.example;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.googlecode.protobuf.socketrpc.RpcChannels;
import com.googlecode.protobuf.socketrpc.RpcConnectionFactory;
import com.googlecode.protobuf.socketrpc.SocketRpcConnectionFactories;
import com.googlecode.protobuf.socketrpc.SocketRpcController;
import com.orbekk.example.CTR;
import com.orbekk.protobuf.Rpc;
import com.orbekk.protobuf.RpcChannel;

import java.util.concurrent.CountDownLatch;

/**
 * Created by pdoviet on 9/30/16.
 */
public class CTRClient {

    public void runClient(int port) {
//        RpcChannel channel = null;
        BlockingRpcChannel channel = null;
        try {
            // Create channel
            RpcConnectionFactory connectionFactory = SocketRpcConnectionFactories
                    .createRpcConnectionFactory("192.168.23.52", 9999);
//            channel = RpcChannel.createOrNull("192.168.23.52", port);
            channel = RpcChannels.newBlockingRpcChannel(connectionFactory);
            // Call service
            CTR.CTRPredictionService.BlockingInterface service = CTR.CTRPredictionService.newBlockingStub(channel);
//            CTR.CTRPredictionService service =
//                    CTR.CTRPredictionService.newStub(channel);

            RpcController controller = new SocketRpcController();
            printData(controller, service);
        } finally {
            if (channel != null) {
//                channel.close();
            }
        }

    }

    public void printData(RpcController controller, CTR.CTRPredictionService.BlockingInterface service) {
//        Rpc rpc = new Rpc();  // Represents a single rpc call.
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            int weekday = 1;
            int hour = 2;
            int bannerId = 3;
            int geographic = 4;
            int zoneId = 5;
            int guid = 6;
            String domain = "xxx";
            int osCode = 8;
            int browserCode = 9;


            CTR.Request request = CTR.Request.newBuilder()
                    .setWeekday(weekday)
                    .setHour(hour)
                    .setBannerId(bannerId)
                    .setGeographic(geographic)
                    .setZoneId(zoneId)
                    .setGuid(guid)
                    .setDomain(domain)
                    .setOsCode(osCode)
                    .setBrowserCode(browserCode)
                    .build();


            // An RPC call is asynchronous. A CountDownLatch is a nice way to wait
            // for the callback to finish.
//            final CountDownLatch doneSignal = new CountDownLatch(1);

            try {

                CTR.Response myResponse = service.getPrediction(controller, request);
                System.out.println(myResponse.getCtrPrediction());
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            System.out.println("Time: " + (System.currentTimeMillis() - startTime));

//            service.getPrediction(rpc, request, new RpcCallback<CTR.Response>() {
//                @Override
//                public void run(CTR.Response reply) {
//                    System.out.println(reply.getBannerId() + ":" + reply.getCtrPrediction());
////                    doneSignal.countDown();
//                }
//            });
//
//            try {
//                rpc.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            try {
//                doneSignal.await();
//            } catch (InterruptedException e) {
//                System.out.println("Interrupted while waiting for fortune. :(");
//            }
        }
    }

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.err.println("Usage: ExampleClient <port>");
//            System.exit(1);
//        }
//        new CTRClient().runClient(Integer.valueOf(args[0]));

//        long startTime = System.currentTimeMillis();
        new CTRClient().runClient(9999);
//        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}
