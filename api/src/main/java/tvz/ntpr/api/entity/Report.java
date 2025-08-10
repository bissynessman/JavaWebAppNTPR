package tvz.ntpr.api.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Report extends BaseEntity {
    private byte[] data;
    private String fileName;
    private String student;

    public Report() {
        super(null);
    }

    public Report(String id, Path pathToFile, String fileName, String student) throws IOException {
        super(id);
        this.data = Files.readAllBytes(pathToFile);
        this.fileName = fileName;
        this.student = student;
    }

    public String getId() {
        return super.getId();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setId(String id) {
        super.setId(id);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void writeToTmpFile(String path) throws IOException {

    }

    public int deleteTmpFile() throws IOException {
        return 0;
    }
}
