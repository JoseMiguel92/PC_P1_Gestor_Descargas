package filedownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloader {
    private static final String downloadFilePath = "https://github.com/jesussanchezoro/PracticaPC/raw/master/descargas.txt";
    private static final String pathOutput = "downloads";

    public FileDownloader() {
        new File(pathOutput).mkdirs();
    }

    public static void process(String downloadsFileURL) {
        try {
            URL website = new URL(downloadsFileURL);
            InputStream in = website.openStream();
            Path pathOut = Paths.get(pathOutput + "/" + "ficherodescargado.txt");
            Files.copy(in, pathOut, StandardCopyOption.REPLACE_EXISTING);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        FileDownloader fd = new FileDownloader();
        fd.process(downloadFilePath);
    }
}
