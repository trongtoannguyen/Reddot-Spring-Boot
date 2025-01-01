package com.reddot.app.service.question;

import com.reddot.app.assembler.QuestionAssembler;
import com.reddot.app.dto.request.QuestionCreateDTO;
import com.reddot.app.dto.request.QuestionUpdateDTO;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.Tag;
import com.reddot.app.entity.User;
import com.reddot.app.entity.enumeration.ROLENAME;
import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.exception.BadRequestException;
import com.reddot.app.exception.ResourceNotFoundException;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.TagRepository;
import com.reddot.app.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
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

    private static boolean isOwner(User user, Question question) {
        if (user == null || question == null) {
            return false;
        }
        return question.getUser().getId().equals(user.getId());
    }

    private static boolean isSuperUser(User user) {
        if (user == null) {
            return false;
        }
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(ROLENAME.ROLE_ADMIN)
                                                         || role.getName().equals(ROLENAME.ROLE_MODERATOR));
    }

    /**
     * Create a new question with a set of maximum 5 tags.
     *
     * @param creatorId the id of the user creating the question
     * @param dto       QuestionPostDTO object containing the question details
     * @return QuestionDTO the created question
     */
    @Transactional
    @Override
    public QuestionDTO questionCreate(Integer creatorId, QuestionCreateDTO dto) throws ResourceNotFoundException {
        try {
            User creator = getUserById(creatorId);
            // question can have at most 5 tags
            Set<Tag> tags = new HashSet<>(5);
            if (!dto.getTags().isEmpty()) {
                for (String tagString : dto.getTags()) {
                    Tag tag = getTagByName(tagString);

                    // increment the number of tag usages
                    tag.incrementTagged();
                    tags.add(tag);
                }
                tagRepository.saveAll(tags);
            }
            Question question = Question.builder()
                    .body(dto.getBody())
                    .title(dto.getTitle())
                    .tags(tags)
                    .user(creator)
                    .build();
            questionRepository.save(question);
            QuestionDTO dto1 = questionAssembler.toDTO(question);

            // custom logic for user-specific properties
            if (creatorId != null) {
                dto1.setUpvoted(isQuestionUpvotedByUser(question.getId(), creatorId));
                dto1.setDownvoted(isQuestionDownvotedByUser(question.getId(), creatorId));
                dto1.setBookmarked(isQuestionBookmarkedByUser(question.getId(), creatorId));
            }
            return dto1;
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuestionDTO questionGetById(Integer id) throws ResourceNotFoundException {
        try {
            Question question = getQuestionById(id);
            return questionAssembler.toDTO(question);
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while retrieving the question: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuestionDTO questionGetWithUser(Integer questionId, @NonNull Integer userId) throws ResourceNotFoundException {
        try {
            Question question = getQuestionById(questionId);
            QuestionDTO dto = questionAssembler.toDTO(question);

            // custom logic for user-specific properties
            dto.setUpvoted(isQuestionUpvotedByUser(questionId, userId));
            dto.setDownvoted(isQuestionDownvotedByUser(questionId, userId));
            dto.setBookmarked(isQuestionBookmarkedByUser(questionId, userId));
            return dto;
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while retrieving the question: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QuestionDTO> questionGetAll() {
        try {
            List<Question> list = questionRepository.findAll();
            return questionAssembler.toDTOList(list);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the questions", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<QuestionDTO> questionGetAllWithUser(@NonNull Integer userId) throws ResourceNotFoundException {
        try {
            Assert.notNull(userId, "User id cannot be null");
            List<Question> list = questionRepository.findAll();
            List<QuestionDTO> dtoList = questionAssembler.toDTOList(list);

            dtoList.forEach(questionDTO -> {
                questionDTO.setUpvoted(isQuestionUpvotedByUser(questionDTO.getQuestionId(), userId));
                questionDTO.setDownvoted(isQuestionDownvotedByUser(questionDTO.getQuestionId(), userId));
                questionDTO.setBookmarked(isQuestionBookmarkedByUser(questionDTO.getQuestionId(), userId));
            });
            return dtoList;
        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while retrieving the questions", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public QuestionDTO questionUpdate(Integer userId, QuestionUpdateDTO dto) throws ResourceNotFoundException, BadRequestException {
        try {
            Question question = getQuestionById(dto.getId());
            User user = getUserById(userId);

            // Check if the user is the owner, admin, or moderator
            boolean isOwner = isOwner(user, question);
            boolean isSuperUser = isSuperUser(user);
            if (!isOwner && !isSuperUser) {
                throw new BadRequestException("You are not permitted to edit this question");
            }
            question.setBody(dto.getBody());
            question.setTitle(dto.getTitle());
            Set<String> newTags = new HashSet<>(dto.getTags());
            List<String> newTagStrings = new ArrayList<>(newTags);

            // get the old tags
            Set<Tag> tags = question.getTags();
            List<String> tagStrings = new ArrayList<>();
            tags.stream().map(Tag::getName).forEach(tagStrings::add);

            // compare the tags with the new tags then increment tag usage
            for (String newTagStr : newTagStrings) {
                if (!tagStrings.contains(newTagStr)) {
                    Tag tag = getTagByName(newTagStr);
                    tag.incrementTagged();
                }
            }
            tags.clear();
            newTagStrings.forEach(newTagStr -> tags.add(getTagByName(newTagStr)));
            question.setTags(tags);
            questionRepository.save(question);
            return questionAssembler.toDTO(question);
        } catch (BadRequestException | ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while updating the question", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void questionDelete(Integer questionId, Integer userId) throws ResourceNotFoundException, BadRequestException {
        try {
            Question question = getQuestionById(questionId);
            User user = getUserById(userId);

            // Check if the user is the owner, admin, or moderator
            boolean isOwner = isOwner(user, question);
            boolean isSuperUser = isSuperUser(user);
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

    private Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question with id `" + id + "` not found"));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id `" + userId + "` not found"));
    }

    private Tag getTagByName(String tagString) {
        return tagRepository.findByName(tagString).orElseThrow(() -> new ResourceNotFoundException("Tag with name `" + tagString + "` not found"));
    }
}
