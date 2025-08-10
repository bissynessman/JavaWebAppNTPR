package tvz.ntpr.core.entity;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Data
public class Report {
    private String id;
    private byte[] data;
    private String fileName;
    private String student;

    public Report() {}

    private Report(String id, Path pathToFile, String fileName, String student) throws IOException {
        this.id = id;
        this.data = Files.readAllBytes(pathToFile);
        this.fileName = fileName;
        this.student = student;
    }

    public static ReportBuilder builder() {
        return new ReportBuilder();
    }

    public static class ReportBuilder {
        private String id = UUID.randomUUID().toString();
        private Path pathToFile;
        private String fileName;
        private String student;

        ReportBuilder() {}

        public ReportBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public ReportBuilder pathToFile(final Path pathToFile) {
            this.pathToFile = pathToFile;
            return this;
        }

        public ReportBuilder fileName(final String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ReportBuilder student(final String student) {
            this.student = student;
            return this;
        }

        public Report build() {
            try {
                return new Report(this.id, this.pathToFile, this.fileName, this.student);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String toString() {
            return "Report.ReportBuilder["
                    + "id=" + this.id
                    + ", pathToFile=" + this.pathToFile
                    + ", fileName=" + this.fileName
                    + ", student=" + this.student
                    + "]";
        }
    }
}
