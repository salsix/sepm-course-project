package at.ac.tuwien.sepm.groupphase.backend.entity.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

@Join(path = "category", alias = "c", type = JoinType.LEFT)
@Join(path = "shows", alias = "s", type = JoinType.LEFT)
@Join(path = "s.hallplan", alias = "h", type = JoinType.LEFT)
@Join(path = "h.location", alias = "l", type = JoinType.LEFT)
@Conjunction({
    @Or({
        @Spec(path = "artists", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "c.category", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "h.name", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "l.name", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "l.country", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "l.zip", params = "s1", spec = LikeIgnoreCase.class),
        @Spec(path = "l.street", params = "s1", spec = LikeIgnoreCase.class)
    }),
    @Or({
        @Spec(path = "artists", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "c.category", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "h.name", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "l.name", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "l.country", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "l.zip", params = "s2", spec = LikeIgnoreCase.class),
        @Spec(path = "l.street", params = "s2", spec = LikeIgnoreCase.class)
    }),
    @Or({
        @Spec(path = "artists", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "c.category", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "h.name", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "l.name", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "l.country", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "l.zip", params = "s3", spec = LikeIgnoreCase.class),
        @Spec(path = "l.street", params = "s3", spec = LikeIgnoreCase.class)
    }),
    @Or({
        @Spec(path = "artists", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "c.category", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "h.name", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "l.name", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "l.country", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "l.zip", params = "s4", spec = LikeIgnoreCase.class),
        @Spec(path = "l.street", params = "s4", spec = LikeIgnoreCase.class)
    }),
    @Or({
        @Spec(path = "artists", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "description", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "name", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "c.category", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "h.name", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "l.name", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "l.country", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "l.zip", params = "s5", spec = LikeIgnoreCase.class),
        @Spec(path = "l.street", params = "s5", spec = LikeIgnoreCase.class)
    })
})
public interface EventSpecification extends Specification<Event> {
}

