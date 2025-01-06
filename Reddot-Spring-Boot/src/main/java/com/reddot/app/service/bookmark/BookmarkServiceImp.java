package com.reddot.app.service.bookmark;

import com.reddot.app.assembler.CommentAssembler;
import com.reddot.app.assembler.QuestionAssembler;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.Bookmark;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.User;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.BookmarkRepository;
import com.reddot.app.repository.CommentRepository;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BookmarkServiceImp implements BookmarkService {
    private static final String ERROR_OCCURRED_BY = "Error occurred by user with id: ";
    private final QuestionRepository questionRepository;
    private final QuestionAssembler questionAssembler;
    private final CommentRepository commentRepository;
    private final CommentAssembler commentAssembler;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkServiceImp(QuestionRepository questionRepository, @Qualifier("questionAssemblerImpl") QuestionAssembler questionAssembler, CommentRepository commentRepository, @Qualifier("commentAssemblerImpl") CommentAssembler commentAssembler, BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.questionAssembler = questionAssembler;
        this.commentRepository = commentRepository;
        this.commentAssembler = commentAssembler;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public QuestionDTO bookmarkQuestion(Integer userId, Integer questionId) {
        try {
            Question question = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);
            if (!bookmarkRepository.existsByUser_IdAndQuestion_Id(userId, questionId)) {

                // we can call user.getBookmarks() to initialize the lazy loading property, but it will make 2 queries to the database, not an ideal to optimize the performance.
                User user = userRepository.findByIdAndFetchBookmarksEagerly(userId).orElseThrow(() -> new ResourceNotFoundException("User with id `" + userId + "` not found"));
                Bookmark bookmark = new Bookmark(user, question);
                user.getBookmarks().add(bookmark);
                bookmarkRepository.save(bookmark);
            } else {
                log.error(ERROR_OCCURRED_BY + userId);
                throw new BadRequestException("Error occurred related to status of bookmarking question");
            }
            return questionAssembler.toDTO(question);
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public QuestionDTO unBookmarkQuestion(Integer userId, Integer questionId) {
        try {
            Question question = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);
            if (bookmarkRepository.existsByUser_IdAndQuestion_Id(userId, questionId)) {
                User user = userRepository.findByIdAndFetchBookmarksEagerly(userId).orElseThrow(() -> new ResourceNotFoundException("User with id `" + userId + "` not found"));
                Bookmark bookmark = bookmarkRepository.findByUser_IdAndQuestion_Id(userId, questionId).orElseThrow(ResourceNotFoundException::new);
                user.getBookmarks().remove(bookmark);
                bookmarkRepository.delete(bookmark);
            } else {
                log.error(ERROR_OCCURRED_BY + userId);
                throw new BadRequestException("Error occurred related to status of bookmarking question");
            }
            return questionAssembler.toDTO(question);
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isBookmarkedByUser(Integer userId, Integer questionId) {
        return bookmarkRepository.existsByUser_IdAndQuestion_Id(userId, questionId);
    }
}
