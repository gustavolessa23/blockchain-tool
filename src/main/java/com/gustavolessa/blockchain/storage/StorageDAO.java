package com.gustavolessa.blockchain.storage;

import com.gustavolessa.blockchain.block.Block;

import java.util.List;

public interface StorageDAO {
    int saveBlock(Block block);
    List<Block> readAll();
    Block findById(long id);
    Block findByHash(String hash);
}
