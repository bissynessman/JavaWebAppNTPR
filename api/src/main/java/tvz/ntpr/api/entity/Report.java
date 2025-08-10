package tvz.ntpr.api.entity;

import java.io.IOException;

public class Report extends BaseEntity {
    private byte[] data;
    private String fileName;
    private String student;

    public Report() {
        super(null);
    }

    public Report(String id, byte[] data, String fileName, String student) throws IOException {
        super(id);
        this.data = data;
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
}
