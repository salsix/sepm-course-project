package at.ac.tuwien.sepm.groupphase.backend.entity.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Disjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import org.springframework.data.jpa.domain.Specification;

@JoinFetch(paths = "hallplan", alias = "h")
@JoinFetch(paths = "event", alias = "e")
@JoinFetch(paths = "h.location", alias = "l")
@Join(path = "event", alias = "e")
@Join(path = "hallplan", alias = "h")
@Join(path = "h.cats", alias = "c")
@Disjunction({
    @And({
        @Spec(path = "hour", params = "earliestH", spec = GreaterThan.class),
        @Spec(path = "hour", params = "latestH", spec = LessThan.class),

        @Spec(path = "date", params = "date", spec = Equal.class),
        @Spec(path = "c.price", params = "maxPrice", spec = LessThanOrEqual.class),
    }),
    @And({
        @Spec(path = "hour", params = "earliestH", spec = Equal.class),
        @Spec(path = "hour", params = "latestH", spec = Equal.class),
        @Spec(path = "minute", params = "earliestM", spec = GreaterThanOrEqual.class),
        @Spec(path = "minute", params = "latestM", spec = LessThanOrEqual.class),

        @Spec(path = "date", params = "date", spec = Equal.class),
        @Spec(path = "c.price", params = "maxPrice", spec = LessThanOrEqual.class),
    }),
    @And({
        @Spec(path = "hour", params = "earliestH", spec = GreaterThan.class),
        @Spec(path = "hour", params = "latestH", spec = Equal.class),
        @Spec(path = "minute", params = "latestM", spec = LessThanOrEqual.class),

        @Spec(path = "date", params = "date", spec = Equal.class),
        @Spec(path = "c.price", params = "maxPrice", spec = LessThanOrEqual.class),
    }),
    @And({
        @Spec(path = "hour", params = "latestH", spec = LessThan.class),
        @Spec(path = "hour", params = "earliestH", spec = Equal.class),
        @Spec(path = "minute", params = "earliestM", spec = GreaterThanOrEqual.class),

        @Spec(path = "date", params = "date", spec = Equal.class),
        @Spec(path = "c.price", params = "maxPrice", spec = LessThanOrEqual.class),
    })
})
public interface ShowSpecification extends Specification<Show> {
}
