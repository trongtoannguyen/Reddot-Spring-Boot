package com.reddot.app.dto.response;

import java.util.HashMap;
import java.util.Set;

/**
 * This type represents a user, but omits many of the fields found on the full User Profile type.
 * This type is mostly analogous to the "user card" found on many pages (like the question page) on the site.
 *
 * @param userId
 * @param displayName
 * @param profileImage
 * @param websiteLink
 * @param badgeCounts  This type represents the total Badges, segregated by rank, a user has earned. e.g.{"bronze": 1,"silver": 2,"gold": 3}
 */
public record ShallowUserDTO(Integer userId, String displayName, String profileImage, String websiteLink,
                             Set<BadgeCount> badgeCounts) {

    // Value Object
    public record BadgeCount(HashMap<String, Integer> badgeCounts) {
    }
}
