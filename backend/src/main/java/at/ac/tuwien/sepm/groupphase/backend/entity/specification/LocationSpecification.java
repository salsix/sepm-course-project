package at.ac.tuwien.sepm.groupphase.backend.entity.specification;

import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@And({
    @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
    @Spec(path = "street", params = "address", spec = LikeIgnoreCase.class),
    @Spec(path = "country", params = "country", spec = LikeIgnoreCase.class),
    @Spec(path = "city", params = "city", spec = LikeIgnoreCase.class),
    @Spec(path = "zip", params = "zip", spec = LikeIgnoreCase.class)
})
public interface LocationSpecification {
}
