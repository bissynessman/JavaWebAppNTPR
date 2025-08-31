package tvz.ntpr.core.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String to;
    private String subject;
    private String body;
    private MultipartFile[] attachments;
}
