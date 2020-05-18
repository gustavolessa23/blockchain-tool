package com.gustavolessa.blockchain.chain;

import com.google.gson.GsonBuilder;
import com.gustavolessa.blockchain.block.Block;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class Blockchain implements Cloneable{

    private List<Block> mined = new ArrayList<>();

    public List<Block> getMined() {
        return mined;
    }


    public Block getById(long id) throws Exception{
        for(Block b: mined){
            if(b.getId() == id) return b;
        }
        throw new Exception();
    }

    public Block getLastBlock() throws Exception {
        if(mined.isEmpty()) throw new Exception();

        return mined.get(mined.size()-1);
    }

    public void replace(Blockchain b){
        this.mined = List.copyOf(b.getMined());
        System.err.println("Blockchain replaced.");
    }

    public boolean add(Block b){
        int sizeBefore = mined.size();
        mined.add(b);
        if(sizeBefore < mined.size()){
            System.err.println("Block added: ID "+b.getId());
            System.err.println("BLOCKCHAIN UPDATED! "+ this);
            return true;
        } else {
            System.err.println("Could not add block: ID "+b.getId());
            return false;
        }
    }

    public int size(){
        return mined.size();
    }

    public Block get(int index){
        return mined.get(index);
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
}
