package filedownloader2;

import java.util.List;

public class Fichero {
    private String filename;
    private int numberOfParts;
    private List<String> parts;

    public Fichero(String filename, List<String> parts) {
        this.filename = filename;
        this.numberOfParts = parts.size();
        this.parts = parts;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(int numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
    }
}
