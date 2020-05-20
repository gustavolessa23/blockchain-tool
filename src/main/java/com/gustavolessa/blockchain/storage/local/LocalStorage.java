package com.gustavolessa.blockchain.storage.local;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.gustavolessa.blockchain.block.Block;
import com.gustavolessa.blockchain.storage.StorageDAO;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class LocalStorage implements StorageDAO {

    static Path path;

    public LocalStorage() {
        this.getPath();
    }

    public void getPath() {
        try {
            path = Paths.get("blockdata");
            if (Files.notExists(path)) Files.createDirectory(path);

        } catch (IOException e) {
        } catch (Exception e) {

        }
    }

    @Override
    public int saveBlock(Block block) {
        try {
            getPath();
            String blockJson = new GsonBuilder().setPrettyPrinting().create().toJson(block);
            Path returned = Files.write(path.resolve(block.getHash()), blockJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            System.out.println("Block saved at: " + returned);
            return 1;
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return 0;
        }
    }

    @Override
    public List<Block> readAll() {
        System.out.println("Reading all blocks from storage...");
        try (Stream<Path> walk = Files.walk(path)) {

            List<Block> result = walk.filter(Files::isRegularFile)
                    .map(x -> getBlock(x))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Block::getTimeStamp))
                    .collect(Collectors.toList());

            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Block>();
        }
    }

    @Override
    public void clear() {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Block getBlock(Path path) {
        Gson gson = new Gson();
        try {
            byte[] content = Files.readAllBytes(path);
            Object obj = gson.fromJson(new String(content), Block.class);
            Block block = (Block) obj;
            System.out.println("Hash read: " + block.getHash());

            return block;
        } catch (JsonSyntaxException e) {
            return null;
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + path.toString());
            return null;
        } catch (IOException e) {
            System.err.println("IOException when reading " + path.toString());
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Block findById(long id) {
        return null;
    }

    @Override
    public Block findByHash(String hash) {
        return null;
    }

    @Override
    public void writeAll(List<Block> blocks) {
        System.out.println("Writing blocks to storage...");
        blocks.stream().forEach(b -> saveBlock(b));
    }
}
