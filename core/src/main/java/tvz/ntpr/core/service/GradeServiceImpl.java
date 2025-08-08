package tvz.ntpr.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.ntpr.core.entity.Grade;
import tvz.ntpr.core.rest.DatabaseApi;
import tvz.ntpr.core.utils.JsonParser;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private static final String API_URL = DatabaseApi.GRADES_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Grade> getGrades() {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getGradesByStudent(String studentId) {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL + "/student/" + studentId,
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getGradeByCourse(String courseId) {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL + "/course/" + courseId,
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public void saveGrade(Grade grade) {
        restTemplate.postForObject(API_URL, grade, Grade.class);
    }

    @Override
    public void updateGrade(Grade grade) {
        restTemplate.put(API_URL, grade);
    }

    @Override
    public void deleteGrades(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(API_URL + "/" + id);
    }
}
