package com.reddot.app.service.moderation;

import com.reddot.app.entity.Comment;
import com.reddot.app.entity.ContentReport;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.User;
import com.reddot.app.repository.CommentRepository;
import com.reddot.app.repository.ContentReportRepository;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContentReportService {

    private final ContentReportRepository contentReportRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ContentReportService(ContentReportRepository contentReportRepository,
                                QuestionRepository questionRepository,
                                CommentRepository commentRepository, UserRepository userRepository) {
        this.contentReportRepository = contentReportRepository;
        this.questionRepository = questionRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // Tạo báo cáo
    public ContentReport createReport(ContentReport report) {
        return contentReportRepository.save(report);
    }

    // Lấy danh sách báo cáo chưa xử lý
    public List<ContentReport> getReports() {
        return contentReportRepository.findAll();
    }

    // Xử lý báo cáo
    @Transactional
    public void resolveReport(Integer reportId, String action) {
        ContentReport report = contentReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Xử lý tùy thuộc vào loại nội dung
        if ("DELETE".equalsIgnoreCase(action)) {
            if (report.getQuestionId() > 0) {
                questionRepository.deleteById(report.getQuestionId());
            } else if (report.getCommentId() > 0) {
                commentRepository.deleteById(report.getCommentId());
            }
        }

        // Xóa báo cáo sau khi xử lý
        contentReportRepository.delete(report);
    }

    @Transactional
    public void processReport(Integer reportId) {
        ContentReport report = contentReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (report.getQuestionId() > 0) {
            // Xử lý báo cáo cho câu hỏi
            Question question = questionRepository.findById(report.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            question.setReportCount(question.getReportCount() + 1);

            if (question.getReportCount() >= 5) {
                questionRepository.delete(question);
            } else {
                sendWarning(question.getUser(), "Your question has been reported.");
            }

            questionRepository.save(question);

        } else if (report.getCommentId() > 0) {
            // Xử lý báo cáo cho bình luận
            Comment comment = commentRepository.findById(report.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            comment.setReportCount(comment.getReportCount() + 1);

            if (comment.getReportCount() >= 5) {
                commentRepository.delete(comment);
            } else {
                sendWarning(comment.getUser(), "Your comment has been reported.");
            }

            commentRepository.save(comment);

        } else {
            // Xử lý báo cáo cho user
            User user = report.getUser();
            user.setViolationCount(user.getViolationCount() + 1);

            if (user.getViolationCount() >= 5) {
                userRepository.delete(user);
            } else {
                sendWarning(user, "You have been reported for inappropriate behavior.");
            }

            userRepository.save(user);
        }

        contentReportRepository.delete(report); // Xóa báo cáo sau khi xử lý
    }

    private void sendWarning(User user, String message) {
        // Giả lập logic gửi email hoặc thông báo
        System.out.println("Warning sent to user " + user.getId() + ": " + message);
    }
}
