package com.reddot.app.service.bookmark;

import com.reddot.app.dto.request.SearchCriteria;
import com.reddot.app.dto.response.BookmarkDTO;
import com.reddot.app.dto.response.QuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookmarkService {

    /**
     * Bookmark a question identified by its id.
     *
     * @param userId     the id of the user bookmarking the question. Implement id of user probably make server has to fetch user from database twice, but its necessary to make sure user is initialized with lazy loading properties.
     * @param questionId the id of the question to be bookmarked
     * @return QuestionDTO object containing the question details
     */
    QuestionDTO bookmarkQuestion(Integer userId, Integer questionId);

    QuestionDTO unBookmarkQuestion(Integer userId, Integer questionId);

    Page<BookmarkDTO> bookmarkGetAllByUserIds(SearchCriteria criteria, List<Integer> uIds);

    boolean isBookmarkedByUser(Integer userId, Integer questionId);
}