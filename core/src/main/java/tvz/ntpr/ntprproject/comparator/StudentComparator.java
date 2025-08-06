package tvz.ntpr.ntprproject.comparator;

import lombok.NoArgsConstructor;
import tvz.ntpr.ntprproject.entity.Student;

import java.util.Comparator;

@NoArgsConstructor
public class StudentComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        int lastNameComparison = s1.getLastName().compareToIgnoreCase(s2.getLastName());
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }
        return s1.getFirstName().compareToIgnoreCase(s2.getFirstName());
    }
}
