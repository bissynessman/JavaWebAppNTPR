package tvz.ntpr.core.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tvz.ntpr.core.entity.Report;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class ReportWrapper {
    private Report report;
    private File dataFile;
    private File signatureFile;
}
