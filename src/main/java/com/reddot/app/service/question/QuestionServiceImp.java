package com.reddot.app.service.question;

import com.reddot.app.assembler.QuestionAssembler;
import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.Role;
import com.reddot.app.entity.Tag;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.TagRepository;
import com.reddot.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImp implements QuestionService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionAssembler questionAssembler;
    private final TagRepository tagRepository;

    //fixme: solution for creating a new question with undefined tags
    @Override
    public QuestionDTO questionCreate(Integer creatorId, QuestionCreateDTO dto) {
        try {
            User creator = getUser(creatorId);
            Set<String> tagStrings = dto.tags();
            Set<Tag> tags = null;
            if (tagStrings != null) {
                // fixme: this is a workaround for creating a new question with undefined tags
                tags = tagStrings.stream().map(tagString -> tagRepository.findByName(tagString).orElseThrow(() -> new ResourceNotFoundException("Tag with name " + tagString + " not found"))).collect(java.util.stream.Collectors.toSet());
            }
            Question question = Question.builder().body(dto.body()).title(dto.title()).tags(tags).user(creator).build();
            questionRepository.save(question);
            // update number of tag usages
            if (tags != null) {
                tags.forEach(tag -> {
                    tag.setTagged(tag.getTagged() + 1);
                    tagRepository.save(tag);
                });
            }

            QuestionDTO dto1 = questionAssembler.toDTO(question);

            // custom logic for user-specific properties
            if (creatorId != null) {
                dto1.setUpvoted(isQuestionUpvotedByUser(question.getId(), creatorId));
                dto1.setDownvoted(isQuestionDownvotedByUser(question.getId(), creatorId));
                dto1.setBookmarked(isQuestionBookmarkedByUser(question.getId(), creatorId));
            }
            return dto1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuestionDTO questionGetWithUser(Integer questionId, Integer userId) throws ResourceNotFoundException {
        try {
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question with id " + questionId + " not found"));
            QuestionDTO dto = questionAssembler.toDTO(question);

            // custom logic for user-specific properties
            if (userId != null) {
                dto.setUpvoted(isQuestionUpvotedByUser(questionId, userId));
                dto.setDownvoted(isQuestionDownvotedByUser(questionId, userId));
                dto.setBookmarked(isQuestionBookmarkedByUser(questionId, userId));
            }
            return dto;
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while retrieving the question", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public QuestionDTO questionGetById(Integer id) throws ResourceNotFoundException {
        try {
            Question question = questionRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
            return questionAssembler.toDTO(question);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while retrieving the question", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void questionDelete(Integer questionId, Integer userId) throws ResourceNotFoundException, BadRequestException {
        try {
            Question question = getQuestion(questionId);
            User user = getUser(userId);
            List<Role> roleList = List.copyOf(user.getRoles());

            // Check if the user is the owner, admin, or moderator
            assert question.getUser().getId() != null;
            boolean isOwner = question.getUser().getId().equals(userId);
            boolean isSuperUser = roleList.stream().anyMatch(role -> role.getName().equals(ROLENAME.ROLE_ADMIN) || role.getName().equals(ROLENAME.ROLE_MODERATOR));
            if (!isOwner && !isSuperUser) {
                throw new BadRequestException("You are not permitted to delete this question");
            }
            if (question.isClosed()) {
                throw new BadRequestException("You cannot delete a closed question");
            }
            questionRepository.delete(question);
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while deleting the question", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isQuestionUpvotedByUser(Integer questionId, Integer userId) {
        return questionRepository.existsByIdAndVotes_UserIdAndVotes_VoteTypeId(questionId, userId, VOTETYPE.UPVOTE.getDirection());
    }

    @Override
    public boolean isQuestionDownvotedByUser(Integer questionId, Integer userId) {
        return questionRepository.existsByIdAndVotes_UserIdAndVotes_VoteTypeId(questionId, userId, VOTETYPE.DOWNVOTE.getDirection());
    }

    @Override
    public boolean isQuestionBookmarkedByUser(Integer questionId, Integer userId) {
        return false;
    }

    private Question getQuestion(Integer id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question with id " + id + " not found"));
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }
}
