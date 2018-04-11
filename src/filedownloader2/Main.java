package filedownloader2;

import java.io.IOException;

public class Main {
    private static final String downloadFilePath = "https://github.com/jesussanchezoro/PracticaPC/raw/master/descargas.txt";

    public static void main(String[] args) {
        int maxDownloads = 2;
        FileDownloader fd = new FileDownloader(maxDownloads);
        System.out.println("Iniciando descargas...");
        try {
            fd.process(downloadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
