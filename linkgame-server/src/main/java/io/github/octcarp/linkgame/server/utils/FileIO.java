package io.github.octcarp.linkgame.server.utils;

import io.github.octcarp.linkgame.common.module.PlayerRecord;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    private final static URL csvFileURL
            = FileIO.class.getResource("player_record.csv");
    private final static CSVFormat csvFormat
            = CSVFormat.DEFAULT.builder().setHeader("Id", "Passwd").build();

    public static File getPlayerListFile() {
        return new File(csvFileURL.getFile());
    }

    public static List<PlayerRecord> readPlayerList() {
        List<PlayerRecord> playerList = new ArrayList<>();
        try (FileReader reader = new FileReader(FileIO.getPlayerListFile())) {
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                String column1 = record.get("Id");
                String column2 = record.get("Passwd");
                playerList.add(new PlayerRecord(column1, column2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    public static boolean updatePlayerByList(List<PlayerRecord> playerList) {
        try (
                PrintWriter writer = new PrintWriter(FileIO.getPlayerListFile());
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {
            for (PlayerRecord player : playerList) {
                csvPrinter.printRecord(player.id(), player.password());
            }
            csvPrinter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
