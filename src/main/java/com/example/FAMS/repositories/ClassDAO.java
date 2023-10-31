package com.example.FAMS.repositories;

import com.example.FAMS.dto.responses.CalendarDayResponse;
import com.example.FAMS.dto.responses.CalendarWeekResponse;
import com.example.FAMS.dto.responses.SearchFilterResponse;
import com.example.FAMS.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ClassDAO extends JpaRepository<Class, String> {

    List<Class> findTop1000ByOrderByCreatedDateDesc();

    @Query(value = "select * from fams.class order by start_date asc, time_from asc", nativeQuery = true)
    List<Class> getCalendarSort();

    @Query(value = "Select * from Class order by modified_date desc", nativeQuery = true)
    List<Class> getAll();

    @Query(value = "SELECT loc.location, fc.time_from as 'timeFrom', fc.time_to as 'timeTo', fc.fsu, fc.status, cu.users_id AS class_user_id, u.name\n" +
            "FROM class AS fc\n" +
            "INNER JOIN class_user AS cu ON fc.class_code = cu.class_id\n" +
            "INNER JOIN users AS u ON cu.users_id = u.user_id\n" +
            "INNER JOIN location AS loc ON fc.class_code = loc.class_code\n" +
            "WHERE u.role = 2;", nativeQuery = true)
    List<SearchFilterResponse> searchByFilter();

    @Query(
            value =
                    "SELECT a.class_id as 'classId', a.class_code as 'classCode', g.time_from as 'timeFrom', g.time_to as 'timeTo', a.start_date as 'startDate', a.end_date as 'endDate', a.status, b.user_type as 'userType', c.name, f.number_of_day as 'numberOfDay'\n"
                            + "FROM class a \n"
                            + "left join class_learning_day g on g.enroll_date = :currentDate and a.class_id = g.class_id\n"
                            + "left join training_programs d on d.training_program_code = a.training_program_code\n"
                            + "left join training_program_syllabuses e on e.training_program_code = d.training_program_code\n"
                            + "left join syllabus f on f.topic_code = e.topic_code\n"
                            + "left join class_user b on a.class_id = b.class_id left join users c on b.users_id = c.user_id \n"
                            + "where b.user_type = 'ADMIN' or b.user_type = 'MENTOR'", nativeQuery = true)
    List<CalendarDayResponse> getCalendarByDay(@Param("currentDate") Date currentDate);

    @Query(
            value =
                    "SELECT a.class_id as 'classId', a.class_code as 'classCode', g.time_from as 'timeFrom', g.time_to as 'timeTo', a.start_date as 'startDate', a.end_date as 'endDate', a.status, g.enroll_date as 'enrollDate'\n"
                            + "FROM class a \n"
                            + "left join class_learning_day g on a.class_id = g.class_id\n"
                            + "WHERE g.enroll_date >= :startDate and g.enroll_date <= :endDate",
            nativeQuery = true)
    List<CalendarWeekResponse> getCalendarByWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
