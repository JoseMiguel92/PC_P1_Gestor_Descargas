package filedownloader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDownloader {
    private SplitAndMerge splitAndMerge = new SplitAndMerge();
    private static final String downloadFilePath = "https://github.com/jesussanchezoro/PracticaPC/raw/master/descargas.txt";
    private static final String pathOutput = "downloads";
    private static String filename;

    public FileDownloader() {
        new File(pathOutput).mkdirs();
    }

    public void parser(String file) throws IOException {
        Map<String, List<String>> map = new HashMap<>();
        List<String> values = null;
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String key = null;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length >= 2) {
                if (!map.containsKey(parts[1])) {
                    key = parts[1];
                    values = new ArrayList<>();
                }
            } else {
                values.add(parts[0]);
                process(parts[0]);
                map.put(key, values);
            }
        }
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            splitAndMerge.mergeFile(pathOutput, e.getKey());
            for (int i = 0; i < e.getValue().size(); i++) {
                Path pathParts = Paths.get(pathOutput + "/" + Paths.get(e.getKey() + ".part" + i));
                Files.deleteIfExists(pathParts);
            }
        }
        reader.close();
    }

    public void process(String downloadsFileURL) throws IOException {
        URL website = new URL(downloadsFileURL);
        InputStream in = website.openStream();
        String[] fileFromUrl = downloadsFileURL.split("/");
        filename = fileFromUrl[fileFromUrl.length - 1];
        Path pathOut = Paths.get(pathOutput + "/" + filename);
        Files.copy(in, pathOut, StandardCopyOption.REPLACE_EXISTING);
        in.close();
    }

    public static void main(String[] args) throws IOException {
        FileDownloader fd = new FileDownloader();
        fd.process(downloadFilePath);
        fd.parser(pathOutput + "/" + filename);
    }
}
