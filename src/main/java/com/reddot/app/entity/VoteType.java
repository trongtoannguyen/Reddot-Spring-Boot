package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.VOTETYPE;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "vote_types")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class VoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private VOTETYPE type;
}
