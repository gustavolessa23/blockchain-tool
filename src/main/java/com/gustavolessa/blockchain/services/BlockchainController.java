package com.gustavolessa.blockchain.services;

import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool2.MiningPool;
import com.gustavolessa.blockchain.pool2.TransactionPool;
import com.gustavolessa.blockchain.pool2.TransmissionPool;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/blockchain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlockchainController {

    @Inject
    Blockchain chain;

    @Inject
    MiningPool miningPool;

    @Inject
    TransactionPool transactionPool;

    @Inject
    TransmissionPool transmissionPool;

    @GET
    public String getBlockchain(){
        return chain.toString();
    }

    @GET
    @Path("/mining")
    public String getMining(){
        return miningPool.toString();
    }

    @GET
    @Path("/transmitting")
    public String getTransmitting(){
        return transmissionPool.toString();
    }

    @GET
    @Path("/transactions")
    public String getTransactions(){
        return transactionPool.toString();
    }

    @POST
    @Path("/transactions")
    public boolean addTransaction(Transaction t){
        return transactionPool.add(t);
    }








}
