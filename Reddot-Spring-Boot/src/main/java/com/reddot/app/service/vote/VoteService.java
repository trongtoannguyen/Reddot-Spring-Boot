package com.reddot.app.service.vote;

import com.reddot.app.dto.request.VoteDto;
import com.reddot.app.entity.Question;
import com.reddot.app.entity.User;
import com.reddot.app.entity.Vote;
import com.reddot.app.entity.VoteType;
import com.reddot.app.entity.enumeration.VOTETYPE;
import com.reddot.app.repository.QuestionRepository;
import com.reddot.app.repository.UserRepository;
import com.reddot.app.repository.VoteRepository;
import com.reddot.app.repository.VoteTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final VoteTypeRepository voteTypeRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository,
                       QuestionRepository questionRepository, VoteTypeRepository voteTypeRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.voteTypeRepository = voteTypeRepository;
    }


    //toggle vote
//       @Transactional
//        public void toggleVote(Integer questionId, String voteTypeName, String username) {
//           User user = userRepository.findByUsername(username)
//                   .orElseThrow(() -> new RuntimeException("User not found"));
//
//           Question question = questionRepository.findById(questionId)
//                   .orElseThrow(() -> new RuntimeException("Question not found"));
//           VoteDto voteDto = null;
//           if (voteDto == null || voteDto.getVoteType() == null || username == null) {
//               throw new IllegalArgumentException("Vote type or username cannot be null");
//           }
//           // Lấy loại vote từ enum (UPVOTE/DOWNVOTE)
//           VOTETYPE voteTypeEnum = VOTETYPE.valueOf(voteTypeName);
//           Optional<VoteType> optionalVoteType = voteTypeRepository.findByType(voteTypeEnum);
//
//           Vote vote = new Vote() ;
//           if (optionalVoteType.isPresent()) {
//               vote.setVoteType(optionalVoteType.get());
//           } else {
//               throw new RuntimeException("VoteType not found");
//           }
//
//           // Kiểm tra vote hiện tại của người dùng
//           Optional<Vote> existingVote = voteRepository.findByUserAndQuestion(user, question);
//
//           VoteType voteType = new VoteType();
//           if (existingVote.isPresent()) {
//               vote = existingVote.get();
//               if (vote.getVoteType().getType() == voteTypeEnum) {
//                   // Nếu đã vote cùng loại → Xóa vote
//                   voteRepository.delete(vote);
//               } else {
//                   // Nếu vote khác loại → Cập nhật vote
//                   vote.setVoteType(voteType);
//                   voteRepository.save(vote);
//               }
//           } else {
//               // Chưa vote → Thêm mới vote
//               Vote newVote = new Vote(user, question, voteType);
//               voteRepository.save(newVote);
//           }
//       }
//    @Transactional
//    public void voteQuestion(Integer questionId, VoteDto voteDto, String username) {
//        // 1. Lấy thông tin người dùng từ username
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Lấy câu hỏi từ database
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new RuntimeException("Question not found"));
//
//        // 3. Lấy loại vote từ VoteDto
//        VOTETYPE voteTypeEnum = VOTETYPE.valueOf(voteDto.getVoteType()); // "UPVOTE" hoặc "DOWNVOTE"
//        VoteType voteType = voteTypeRepository.findByType(voteTypeEnum)
//                .orElseThrow(() -> new RuntimeException("VoteType not found"));
//
//        // 4. Kiểm tra xem người dùng đã vote câu hỏi này chưa
//        Optional<Vote> existingVote = voteRepository.findByUserAndQuestion(user, question);
//
//        if (existingVote.isPresent()) {
//            // Nếu đã vote, kiểm tra loại vote
//            Vote vote = existingVote.get();
//            if (vote.getVoteType().equals(voteType)) {
//                // Nếu đã vote cùng loại → Hủy vote (xóa)
//                voteRepository.delete(vote);
//            } else {
//                // Nếu vote khác loại → Cập nhật loại vote
//                vote.setVoteType(voteType);
//                voteRepository.save(vote);
//            }
//        } else {
//            // Nếu chưa vote → Thêm mới vote
//            Vote newVote = new Vote(user, question, voteType);
//            voteRepository.save(newVote);
//        }
//    }
    @Transactional
    public void voteQuestion(Integer questionId, VoteDto voteDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        VOTETYPE voteTypeEnum = VOTETYPE.valueOf(voteDto.getVoteType().toUpperCase());
        VoteType voteType = voteTypeRepository.findByType(voteTypeEnum)
                .orElseThrow(() -> new RuntimeException("Vote type not found"));

        Optional<Vote> existingVote = voteRepository.findByUserAndQuestion(user, question);

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.getVoteType().equals(voteType)) {
                voteRepository.delete(vote);
            } else {
                vote.setVoteType(voteType);
                voteRepository.save(vote);
            }
        } else {
            Vote newVote = new Vote(user, question, voteType);
            voteRepository.save(newVote);
        }
    }


}

