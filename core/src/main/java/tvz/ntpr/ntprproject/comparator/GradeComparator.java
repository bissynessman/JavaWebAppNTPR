package tvz.ntpr.ntprproject.comparator;

import tvz.ntpr.ntprproject.entity.Grade;

import java.util.Comparator;

public class GradeComparator implements Comparator<Grade> {
    @Override
    public int compare(Grade g1, Grade g2) {
        return g1.getGrade().compareTo(g2.getGrade());
    }
}
