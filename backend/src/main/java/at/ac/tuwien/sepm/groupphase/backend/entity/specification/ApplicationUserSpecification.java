package at.ac.tuwien.sepm.groupphase.backend.entity.specification;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Conjunction({
    @Or(@Spec(path = "firstName", params = "firstname", spec = LikeIgnoreCase.class)),
    @Or(@Spec(path = "lastName", params = "lastname", spec = LikeIgnoreCase.class)),
    @Or(@Spec(path = "email", params = "email", spec = LikeIgnoreCase.class)),
    @Or(@Spec(path = "admin", params = "admin", spec = Equal.class)),
    @Or({
        @Spec(path = "locked", params = "locked", spec = Equal.class),
        @Spec(path = "loginFails", params = "loginFails", spec = Equal.class)
    })

})
public interface ApplicationUserSpecification extends Specification<ApplicationUser> {
}
