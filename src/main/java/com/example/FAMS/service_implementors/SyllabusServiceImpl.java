package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.base.Strings;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    String line = "";

    @Override
    public List<Syllabus> getSyllabuses() {
        return syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public List<Syllabus> getDetailSyllabus() {
        return syllabusDAO.findAll();
    }


    @Override
    public Syllabus createSyllabus(String topicName, String topicCode, String version, int numberOfAudience) {
        Syllabus syllabus = Syllabus.builder()
                .version(version)
                .topicCode(topicCode)
                .topicName(topicName)
                .trainingAudience(numberOfAudience)
                .build();

        syllabusDAO.save(syllabus);
        return syllabus;
    }

    @Override
    public Syllabus updateSyllabus(UpdateSyllabusRequest updatesyllabusRequest) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(updatesyllabusRequest.getTopicCode());
        Syllabus syllabusexits = optionalSyllabus.orElse(null);
        if (syllabusexits != null) {
            syllabusexits.setTopicName(updatesyllabusRequest.getTopicName());
            syllabusexits.setTechnicalGroup(updatesyllabusRequest.getTechnicalGroup());
            syllabusexits.setVersion(updatesyllabusRequest.getVersion());
            syllabusexits.setTrainingAudience(updatesyllabusRequest.getTrainingAudience());
            syllabusexits.setTopicOutline(updatesyllabusRequest.getTopicOutline());
            syllabusexits.setTrainingMaterials(updatesyllabusRequest.getTrainingMaterials());
            syllabusexits.setPriority(updatesyllabusRequest.getPriority());
            syllabusexits.setPublishStatus(updatesyllabusRequest.getPublishStatus());
            syllabusexits.setCreatedBy(updatesyllabusRequest.getCreatedBy());
            syllabusexits.setCreatedDate(updatesyllabusRequest.getCreatedDate());
            syllabusexits.setModifiedBy(updatesyllabusRequest.getModifiedBy());
            syllabusexits.setModifiedDate(updatesyllabusRequest.getModifiedDate());


            Syllabus syllabusUpdate = syllabusDAO.save(syllabusexits);

            if (syllabusUpdate != null) {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus successful")
                        .updateSyllabus(syllabusUpdate)
                        .build().getUpdateSyllabus();


            } else {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus failed")
                        .updateSyllabus(null)
                        .build().getUpdateSyllabus();

            }


        } else {
            return UpdateSyllabusResponse.builder()
                    .status("Syllabus not found")
                    .updateSyllabus(null)
                    .build().getUpdateSyllabus();

        }

    }

    @Override
    public Syllabus getSyllabusById(String topicCode) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        return optionalSyllabus.orElse(null);
    }

    @Override
    public List<Syllabus> processDataFromCSV(MultipartFile file) throws IOException {
        List<Syllabus> syllabusList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Syllabus c = new Syllabus();

                c.setTopicCode(data[0]);
                c.setCreatedBy(data[1]);

                // Chuyển đổi từ chuỗi ngày thành Date và chỉ lấy phần ngày
                Date parsedDate = dateFormat.parse(data[2]);
                c.setCreatedDate(new java.sql.Date(parsedDate.getTime()));

                c.setModifiedBy(data[3]);
                c.setModifiedDate(new java.sql.Date(dateFormat.parse(data[4]).getTime()));
                c.setPriority(data[5]);
                c.setPublishStatus(data[6]);
                c.setTechnicalGroup(data[7]);
                c.setTopicName(data[8]);
                c.setTopicOutline(data[9]);
                c.setTrainingAudience(Integer.parseInt(data[10]));
                c.setTrainingMaterials(data[11]);
                c.setTrainingPrinciples(data[12]);
                c.setVersion(data[13]);
//                c.setUserID(data[14]);
                syllabusList.add(c);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return syllabusList;
    }

    @Override
    public Syllabus duplicateSyllabus(String topicCode) {
        return syllabusDAO.getLastSyllabusByTopicCode(topicCode);
    }
    public Syllabus saveSyllabus(Syllabus syllabus) {
        return syllabusDAO.save(syllabus);
    }

    public List<Syllabus> searchSyllabus(String createdDate, String searchValue, String orderBy) {
        List<Syllabus> syllabusList = syllabusDAO.findAll();
        if(!Strings.isNullOrEmpty(createdDate)){
            syllabusList = syllabusList.stream().filter(n -> {
                return new SimpleDateFormat("yyyy-MM-dd").format(n.getCreatedDate()).equals(createdDate);
            }).collect(Collectors.toList());
        }
        if(!Strings.isNullOrEmpty(searchValue)){
            syllabusList = syllabusList.stream().filter(n -> n.getTopicName().trim().toLowerCase().contains(searchValue.trim().toLowerCase())
                    || n.getTopicCode().trim().toLowerCase().contains(searchValue.trim().toLowerCase())).collect(Collectors.toList());
        }
        return syllabusList;
    }

}
