package tvz.ntpr.ntprproject.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tvz.ntpr.ntprproject.entity.Course;

@Getter
@Setter
@NoArgsConstructor
public class CourseWrapper {
    private Course course = Course.builder().build();
    private String professorId;
}
