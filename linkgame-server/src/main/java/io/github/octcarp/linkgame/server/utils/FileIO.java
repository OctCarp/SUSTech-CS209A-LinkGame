package io.github.octcarp.linkgame.server.utils;

import io.github.octcarp.linkgame.common.module.Player;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileIO {
    //TODO: Save .csv persistently, instead of build refresh every time

    private final static URL csvFileURL
            = FileIO.class.getResource(ServerConfig.getPlayerRecordCsvPath());

    public static File getPlayerListFile() {
        return new File(csvFileURL.getFile());
    }

    public static List<Player> readPlayerList() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().
                setHeader("Id", "Passwd").setSkipHeaderRecord(true).build();
        List<Player> playerList = new CopyOnWriteArrayList<>();

        try (FileReader reader = new FileReader(FileIO.getPlayerListFile())) {
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                String id = record.get("Id");
                String passwd = record.get("Passwd");
                playerList.add(new Player(id, passwd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    public synchronized static boolean updatePlayerByList(List<Player> playerList) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader("ID", "Password").build();
        try (
                PrintWriter writer = new PrintWriter(FileIO.getPlayerListFile());
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {
            for (Player player : playerList) {
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
