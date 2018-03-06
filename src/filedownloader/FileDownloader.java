package filedownloader;

import java.io.IOException;

public class FileDownloader {
    private static final String downloadFilePath = "https://github.com/jesussanchezoro/PracticaPC/raw/master/descargas.txt";

    public FileDownloader() {
        throw new RuntimeException("Not yet implemented");
    }

    public void process(String downloadsFileURL) {
        throw new RuntimeException("Not yet implemented");
    }

    public static void main(String[] args) throws IOException {
        String downloadFile = downloadFilePath;
        FileDownloader fd = new FileDownloader();
        fd.process(downloadFile);
    }
}
