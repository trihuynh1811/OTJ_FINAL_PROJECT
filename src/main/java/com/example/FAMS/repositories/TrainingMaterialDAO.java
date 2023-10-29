package com.example.FAMS.repositories;

import com.example.FAMS.models.TrainingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingMaterialDAO extends JpaRepository<TrainingMaterial, Integer> {

    @Query("""
                SELECT TM FROM TrainingMaterial AS TM WHERE TM.syllabus.topicCode = :topicCode
            """)
    List<TrainingMaterial> findTrainingMaterialBySyllabus(String topicCode);

    @Query("SELECT TM FROM TrainingMaterial AS TM WHERE TM.material = :material")
    TrainingMaterial findTrainingMaterialByMaterial(String material);

    @Query("DELETE FROM TrainingMaterial WHERE TrainingMaterial.material = :material")
    void deleteTrainingMaterialByMaterial(String material);

}
