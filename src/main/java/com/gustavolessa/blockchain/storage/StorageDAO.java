package com.gustavolessa.blockchain.storage;

import com.gustavolessa.blockchain.block.Block;

import java.util.List;

/**
 * Data Access Object interface, to allow any data source to be used for blocks persistence.
 */
public interface StorageDAO {
    int saveBlock(Block block);

    List<Block> readAll();

    void writeAll(List<Block> blocks);

    Block findById(long id);

    Block findByHash(String hash);

    void clear();
}
