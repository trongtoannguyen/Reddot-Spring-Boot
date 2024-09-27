package com.reddot.app.repository;

import com.reddot.app.entity.ContentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentReportRepository extends JpaRepository<ContentReport, Integer> {
}
