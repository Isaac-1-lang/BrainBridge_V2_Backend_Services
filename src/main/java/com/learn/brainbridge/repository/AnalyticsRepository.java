package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Projects, Integer> {


    @Query("SELECT p.viewCount FROM Projects p WHERE p.id = :projectId")
    Integer getViewCount(@Param("projectId") Integer projectId);

    @Query("SELECT p.enterpriseRequests FROM Projects p WHERE p.id = :projectId")
    Integer getEnterpriseRequests(@Param("projectId") Integer projectId);

 

    @Query("SELECT SUM(p.viewCount) FROM Projects p")
    Long getTotalViews();

    @Query("SELECT SUM(p.enterpriseRequests) FROM Projects p")
    Long getTotalEnterpriseRequests();

    @Query("SELECT COUNT(p) FROM Projects p")
    Long getTotalProjects();



    @Query("SELECT COUNT(p) FROM Projects p WHERE p.projectStatus = :status")
    Long countByStatus(@Param("status") Enum status);

   

    @Query("SELECT COUNT(p) FROM Projects p WHERE p.projectVisibility = :visibility")
    Long countByVisibility(@Param("visibility") Enum visibility);


    @Query("SELECT p FROM Projects p ORDER BY p.viewCount DESC")
    List<Projects> findTopByViews(org.springframework.data.domain.Pageable pageable);

  

    @Query("SELECT p FROM Projects p ORDER BY p.createdAt DESC")
    List<Projects> findRecentProjects(org.springframework.data.domain.Pageable pageable);
}