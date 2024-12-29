package com.reddot.app.repository;

import com.reddot.app.entity.ContentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentReportRepository extends JpaRepository<ContentReport, Integer> {
    // Lấy danh sách các báo cáo chưa xử lý
    List<ContentReport> findByQuestionIdAndCommentId(int questionId, int commentId);

    // Lấy báo cáo dựa trên user
    List<ContentReport> findByUser_Id(int userId);

}
