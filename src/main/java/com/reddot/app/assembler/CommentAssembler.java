package com.reddot.app.assembler;

import com.reddot.app.assembler.decorator.CommentMapperDecorator;
import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserAssembler.class})
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentAssembler {

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "questionId", source = "question.id", ignore = true)
    @Mapping(target = "responseTo", source = "responseTo", resultType = Integer.class)
    @Mapping(target = "author", source = "user")
    @Mapping(target = "creationDate", source = "createdAt")
    @Mapping(target = "lastEditDate", source = "updatedAt")
    @Mapping(target = "upvoted", ignore = true)
    @Mapping(target = "downvoted", ignore = true)
    CommentDTO toDTO(Comment comment);

    default Integer mapResponseTo(Comment comment) {
        if (comment == null) {
            return null;
        }
        return comment.getResponseTo() == null ? null : comment.getResponseTo().getId();
    }
}
