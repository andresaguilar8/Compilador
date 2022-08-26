package FileHandler;

import java.io.*;

public class FileHandler {

    private File file;
    private int currentRowNumber = 1;
    private int currentColumnNumber = 0;
    private int currentCharacter;
    private int previousCharacter;
    private String rowWithError;
    BufferedReader fileReader;

    public FileHandler(File file) throws IOException {
        this.file = file;
        fileReader = new BufferedReader(new FileReader(file));
    }

    public int readNextCharacter() throws IOException {
        this.previousCharacter = this.currentCharacter;
        this.currentCharacter =  this.fileReader.read();

//        if (this.currentCharacter != -1)
            this.currentColumnNumber += 1;

        if (this.previousCharacter == '\n') {
            this.currentRowNumber += 1;
            this.currentColumnNumber = 1;
        }

        //leo los enter
        if (this.currentCharacter == '\r')
            this.currentCharacter = this.fileReader.read();

        return this.currentCharacter;
    }

    public int getCurrentRowNumber() {
        return this.currentRowNumber;
    }

    public String getRowWithError() throws IOException {
        BufferedReader fileReaderForLineError = new BufferedReader(new FileReader(file));

        int rowNumber = 1;

        while (rowNumber < this.currentRowNumber) {
            fileReaderForLineError.readLine();
            rowNumber += 1;
        }

        this.rowWithError = fileReaderForLineError.readLine();

        if (this.rowWithError == null)
            this.rowWithError = "";

        return this.rowWithError;
    }

    public int getCurrentColumnNumber() {
        return this.currentColumnNumber;
    }
    public int getCurrentCharacter() {
        return this.currentCharacter;
    }

}
