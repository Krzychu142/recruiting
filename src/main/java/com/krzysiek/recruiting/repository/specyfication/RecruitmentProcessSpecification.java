package com.krzysiek.recruiting.repository.specyfication;

import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RecruitmentProcessSpecification {

    public static Specification<RecruitmentProcess> buildSpecification(Long userId, RecruitmentProcessStatus status, Long cvId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (cvId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cv").get("id"), cvId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
