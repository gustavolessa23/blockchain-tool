package com.gustavolessa.blockchain.chain;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@ApplicationScoped
public class Blockchain implements Cloneable{

    private List<Block> blockchain = new ArrayList<>();

    public List<Block> getAll() {
        return blockchain;
    }


    public Block getById(long id) throws Exception{
        for(Block b: blockchain){
            if(b.getId() == id) return b;
        }
        throw new Exception();
    }

    public Block getLastBlock() throws Exception {
        if(blockchain.isEmpty()) throw new Exception();

        return blockchain.get(blockchain.size()-1);
    }

    public void replace(Blockchain b){
        this.blockchain = List.copyOf(b.getAll());
        System.err.println("Blockchain replaced.");
    }

    public boolean add(Block b){
        int sizeBefore = blockchain.size();
        blockchain.add(b);
        if(sizeBefore < blockchain.size()){
            System.err.println("Block ID "+b.getId()+" added to the blockchain.");
            return true;
        } else {
            System.err.println("Could not add block ID "+b.getId());
            return false;
        }
    }

    public boolean contains(Block b){
        return blockchain.contains(b);
    }

    public int size(){
        return blockchain.size();
    }

    public Block get(int index){
        return blockchain.get(index);
    }

    @Override
    public Object clone() {
        try {
            return (Blockchain) super.clone();
        } catch (CloneNotSupportedException e) {
            Blockchain tmp = new Blockchain();
            tmp.replace(this);
            return tmp;
        }
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public void reset(){
        blockchain = List.copyOf(blockchain).subList(0, 0);

//        for(Block b : blockchain){
////            if(blockchain.indexOf(b) > 0) {
////                try{
////                    blockchain.remove(b);
////                }catch (ConcurrentModificationException e){
////
////                }
////
////            }
//        }
    }
}
