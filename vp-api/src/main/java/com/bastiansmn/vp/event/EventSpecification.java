package com.bastiansmn.vp.event;

import com.bastiansmn.vp.common.specifiation.BaseSpecification;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.user.UserDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class EventSpecification extends BaseSpecification<EventDAO> {

    public static final String DATE_START = "date_start";
    public static final String DATE_END = "date_end";
    public static final String PROJECT_ID = "projectId";
    public static final String USER_ID = "user_id";

    @Override
    public Specification<EventDAO> getFilter(Map<String, String> filters) {
        return (root, query, cb) -> where(searchedCriteria(filters)).toPredicate(root, query, cb);
    }

    Specification<EventDAO> searchedCriteria(Map<String, String> searchField) {
        return eventDateAfter(DATE_START, searchField.get("from"))
          .and(eventDateBefore(DATE_END, searchField.get("to")))
          .and(projectEquals(PROJECT_ID, searchField.get("project_id")))
          .and(userEquals(USER_ID, searchField.get("user_id")));
    }

    private Specification<EventDAO> projectEquals(String projectId, String project_id_val) {
        return (root, query, cb) -> {
            if (project_id_val == null) {
                return null;
            }
            Join<EventDAO, ProjectDAO> eventJoinProject = root.join("project");
            return cb.equal(eventJoinProject.get(projectId), project_id_val);
        };
    }

    private Specification<EventDAO> userEquals(String userId, String user_id_val) {
        return (root, query, cb) -> {
            try {
                var userIDParsed = Long.parseLong(user_id_val);
                Join<EventDAO, UserDAO> eventJoinUser = root.join("participants");
                return cb.equal(eventJoinUser.get(userId), userIDParsed);
            } catch (NumberFormatException e) {
                return null;
            }
        };

    }

    private Specification<EventDAO> eventDateBefore(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }
            var date = LocalDateTime.parse(value.replace("Z", ""));
            return cb.lessThanOrEqualTo(root.get(attribute), new Date(date.toInstant(ZoneOffset.UTC).toEpochMilli()));
        };
    }

    private Specification<EventDAO> eventDateAfter(String attribute, String value) {
        return (root, query, cb) -> {
            if (value == null) {
                return null;
            }
            
            var date = LocalDateTime.parse(value.replace("Z", ""));
            return cb.greaterThanOrEqualTo(root.get(attribute), new Date(date.toInstant(ZoneOffset.UTC).toEpochMilli()));
        };
    }

}
