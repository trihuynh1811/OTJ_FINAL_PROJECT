package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.UpdateClassResponse;
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
    public List<Class> getDetailClass() {
        return classDAO.findAll();
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
    public UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest) {
        Optional<Class> optionalClass = classDAO.findById(updateClassRequest.getClassId());
        Class existingClass = optionalClass.orElse(null);
        if (existingClass != null) {
            existingClass =
                    Class.builder()
                            .classId(existingClass.getClassId())
                            .className(updateClassRequest.getClassName())
                            .classCode(updateClassRequest.getClassCode())
                            .duration(updateClassRequest.getDuration())
                            .location(updateClassRequest.getLocation())
                            .startDate(updateClassRequest.getStartDate())
                            .endDate(updateClassRequest.getEndDate())
                            .createdBy(existingClass.getCreatedBy())
                            .createdDate(existingClass.getCreatedDate())
                            .modifiedBy(existingClass.getModifiedBy())
                            .modifiedDate(existingClass.getModifiedDate())
                            .build();

            Class updatedClass = classDAO.save(existingClass);

            if (updatedClass != null) {
                return UpdateClassResponse.builder()
                        .status("Update Class successful")
                        .updatedClass(updatedClass)
                        .build();
            } else {
                // Xử lý nếu việc cập nhật thất bại
                return UpdateClassResponse.builder()
                        .status("Update Class failed")
                        .updatedClass(null)
                        .build();
            }
        } else {
            // Xử lý nếu lớp học không tồn tại
            return UpdateClassResponse.builder()
                    .status("Class not found")
                    .updatedClass(null)
                    .build();
        }
    }

    public List<Class> getDetailClasses() {
        return classDAO.findAll();
    }

    @Override
    public Class getClassById(int classId) {
        Optional<Class> optionalClass = classDAO.findById(classId);
        return optionalClass.orElse(null);
    }
}
