package de.tub.grpc;

import de.tub.Dummy;
import de.tub.DummyServiceGrpc;
import de.tub.StockOuterClass;
import de.tub.grpc.auth.AuthEncoder;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class SimpleClient {
    static final String targetServer1 = "141.23.28.222:60003";
    static final String targetServer2 = "141.23.28.222:60002";
    static final String isisEmail = "p.d.q.bach@campus.tu-berlin.de";
    static final String isisMatrikelNr = "1234567";
    static final String authString = AuthEncoder.generateAuthString(isisEmail, isisMatrikelNr);


    public static void main(String[] args) {
        // This is a very simple client demonstrating how to send a request to the server
        // Please replace the MatrikelNr and E-Mail fields with your MatrikelNr and E-Mail first, and then check whether the Call works (i.e., does not return an error)
        // Since we are using a service from server1 (see the two folders in the proto/ folder), we need to create a connection to server1.
        // If you want to use services from server2, you need to create a channel to targetServer2
        ManagedChannel channel = Grpc.newChannelBuilder(targetServer1, InsecureChannelCredentials.create()).build();
        DummyServiceGrpc.DummyServiceBlockingStub stub = DummyServiceGrpc.newBlockingStub(channel);

        try {

            System.out.println(stub.dummy(Dummy.DummyRequest.newBuilder().setAuth(authString).setInput(1337).build()));

        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown();
        }
    }
}
