package com.gustavolessa.blockchain.services;

import com.gustavolessa.blockchain.chain.Blockchain;
import com.gustavolessa.blockchain.pool.MiningPool;
import com.gustavolessa.blockchain.pool.TransactionPool;
import com.gustavolessa.blockchain.pool.TransmissionPool;
import com.gustavolessa.blockchain.transaction.Transaction;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
