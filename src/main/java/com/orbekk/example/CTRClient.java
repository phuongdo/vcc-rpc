package com.orbekk.example;

import com.google.protobuf.RpcCallback;
import com.orbekk.example.CTR;
import com.orbekk.protobuf.Rpc;
import com.orbekk.protobuf.RpcChannel;

import java.util.concurrent.CountDownLatch;

/**
 * Created by pdoviet on 9/30/16.
 */
public class CTRClient {

    public void runClient(int port) {
        RpcChannel channel = null;
        try {
            channel = RpcChannel.createOrNull("localhost", port);
            CTR.CTRPredictionService service =
                    CTR.CTRPredictionService.newStub(channel);
            printData(service);
        } finally {
            if (channel != null) {
                channel.close();
            }
        }

    }

    public void printData(CTR.CTRPredictionService service) {
        Rpc rpc = new Rpc();  // Represents a single rpc call.

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
        final CountDownLatch doneSignal = new CountDownLatch(1);
        service.getPrediction(rpc, request, new RpcCallback<CTR.Response>() {
            @Override
            public void run(CTR.Response reply) {
                System.out.println(reply.getBannerId() + ":" + reply.getCtrPrediction());
                doneSignal.countDown();
            }
        });

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for fortune. :(");
        }
    }

    public static void main(String[] args) {
//        if (args.length == 0) {
//            System.err.println("Usage: ExampleClient <port>");
//            System.exit(1);
//        }
//        new CTRClient().runClient(Integer.valueOf(args[0]));
        new CTRClient().runClient(9999);
    }
}
