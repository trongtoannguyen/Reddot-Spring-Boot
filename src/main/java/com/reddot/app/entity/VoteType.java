package com.reddot.app.entity;

import com.reddot.app.entity.enumeration.VOTETYPE;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @NonNull
    private VOTETYPE type;
}
