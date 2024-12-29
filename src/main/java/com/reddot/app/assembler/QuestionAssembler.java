package com.reddot.app.assembler;

import com.reddot.app.assembler.decorator.QuestionMapperDecorator;
import com.reddot.app.dto.response.QuestionDTO;
import com.reddot.app.entity.Question;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * QuestionAssembler class is used to convert Question entity to QuestionDTO record and vice versa.
 */
@Mapper(componentModel = "spring", uses = {UserAssembler.class, CommentAssembler.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(QuestionMapperDecorator.class)
public interface QuestionAssembler {

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "commentCount", source = "question")
    @Mapping(target = "commentList", source = "question.comments")
    @Mapping(target = "lastEditDate", source = "question.updatedAt")
    @Mapping(target = "creationDate", source = "question.createdAt")
    @Mapping(target = "closeDate", source = "question.closedAt")
    @Mapping(target = "upvoted", ignore = true)
    @Mapping(target = "downvoted", ignore = true)
    @Mapping(target = "bookmarked", ignore = true)
    QuestionDTO toDTO(Question question);

    // count total number of comments
    default Integer countComments(Question question) {
        // https://mapstruct.org/documentation/stable/reference/html/#invoking-custom-mapping-method
        return (question.getComments() == null) ? 0 : question.getComments().size();
    }
}
