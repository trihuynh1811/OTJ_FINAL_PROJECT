package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.models.Class;
import com.example.FAMS.repositories.ClassDAO;
import com.example.FAMS.services.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassDAO classDAO;

    @Override
    public List<Class> getClasses() {
        return classDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public Class createClass(String className, String classCode, String duration, String location, Date startDate, Date endDate, String createdBy) {
        Class classInfo = Class.builder()
                .className(className)
                .classCode(classCode)
                .duration(duration)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .createdBy(createdBy)
                .build();

        classDAO.save(classInfo);
        return classInfo;
    }

    @Override
    public Class updateClass(UpdateClassRequest updateClassRequest) {
        Optional<Class> optionalClass = classDAO.findById(updateClassRequest.getClassId());
        Class existingClass = optionalClass.orElse(null);
        if (existingClass != null) {
            existingClass.setClassName(updateClassRequest.getClassName());
            existingClass.setClassCode(updateClassRequest.getClassCode());
            existingClass.setDuration(updateClassRequest.getDuration());
            existingClass.setLocation(updateClassRequest.getLocation());
            existingClass.setStartDate(updateClassRequest.getStartDate());
            existingClass.setEndDate(updateClassRequest.getEndDate());

            Class updatedClass = classDAO.save(existingClass);

            if (updatedClass != null) {
                return updatedClass;
            } else {
                // Xử lý nếu việc cập nhật thất bại
                return null;
            }
        } else {
            // Xử lý nếu lớp học không tồn tại
            return null;
        }
    }

    @Override
    public Class getClassById(int classId) {
        Optional<Class> optionalClass = classDAO.findById(classId);
        return optionalClass.orElse(null);
    }

}
