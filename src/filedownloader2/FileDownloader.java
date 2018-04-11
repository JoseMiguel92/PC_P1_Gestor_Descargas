package filedownloader2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class FileDownloader {
    private SplitAndMerge splitAndMerge = new SplitAndMerge();
    private static final String pathOutput = "downloads";
    private List<Thread> threads;
    private BlockingQueue<String> blockingQueue;
    private CountDownLatch countDownLatch;

    public FileDownloader(int maxDownloads) {
        new File(pathOutput).mkdirs();
        this.blockingQueue = new LinkedBlockingQueue<>();
        this.threads = new ArrayList<>();
        for (int i = 0; i < maxDownloads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    download();
                } catch (InterruptedException | IOException e) {
                    System.out.println("Se ha interrumpido el hilo");
                }
            }, "Hilo " + i);
            threads.add(thread);
            thread.start();
        }
    }

    public void process(String downloadsFileURL) throws IOException, InterruptedException {
        String filename = getNameFile(downloadsFileURL);
        URL website = new URL(downloadsFileURL);
        InputStream in = website.openStream();
        Path pathOut = Paths.get(pathOutput + "/" + filename);
        Files.copy(in, pathOut, StandardCopyOption.REPLACE_EXISTING);
        in.close();
        List<Fichero> parsedFile = parser(filename);
        for (Fichero fichero : parsedFile) {
            this.countDownLatch = new CountDownLatch(fichero.getNumberOfParts());
            for (String part : fichero.getParts()) {
                System.out.println("Blocking");
                this.blockingQueue.put(part);
            }
            this.countDownLatch.await();
            //Mergeo las partes
            parsedFile.forEach(fich -> splitAndMerge.mergeFile(pathOutput, fich.getFilename()));

            //Borro las partes
            deleteFiles(parsedFile);
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    private List<Fichero> parser(String filename) throws IOException {
        Scanner scanner = new Scanner(new FileReader(pathOutput + "/" + filename));
        String line;
        List<Fichero> allFiles = new ArrayList<>();
        String fichName = null;
        List<String> partsOfFile = null;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] parts = line.split(" ");
            if (parts.length > 1) {
                if (fichName == null) {
                    fichName = parts[1];
                    partsOfFile = new ArrayList<>();
                } else {
                    Fichero file = new Fichero(fichName, partsOfFile);
                    allFiles.add(file);
                    System.out.println("Añadido fichero: " + fichName);
                    fichName = parts[1];
                    partsOfFile = new ArrayList<>();
                }
            } else {
                partsOfFile.add(parts[0]);
            }
        }
        Fichero file = new Fichero(fichName, partsOfFile);
        allFiles.add(file);
        System.out.println("Añadido fichero: " + fichName);
        scanner.close();
        return allFiles;
    }

    private void download() throws IOException, InterruptedException {
        while (true) {
            String url = this.blockingQueue.take();
            System.out.println("DESCARGANDO: " + url);
            URL website = new URL(url);
            InputStream in = website.openStream();
            Path pathOut = Paths.get(pathOutput + "/" + getNameFile(url));
            Files.copy(in, pathOut, StandardCopyOption.REPLACE_EXISTING);
            in.close();
            //TODO ESPERO PARA COMPROBAR QUE FUNCIONA
            Thread.sleep(2000);
            this.countDownLatch.countDown();
            System.out.println("TERMINADO: " + url);

        }
    }

    private void deleteFiles(List<Fichero> parsedFile) throws IOException {
        for (Fichero fich : parsedFile) {
            for (int i = 0; i < fich.getNumberOfParts(); i++) {
                Path pathParts = Paths.get(pathOutput + "/" + Paths.get(fich.getFilename() + ".part" + i));
                Files.deleteIfExists(pathParts);
            }
        }
    }

    private String getNameFile(String fullUrl) {
        String[] fileFromUrl = fullUrl.split("/");
        return fileFromUrl[fileFromUrl.length - 1];
    }

}
