package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hallplan;

public interface HallplanService {
    /**
     * get hallplan by id.
     *
     * @param id hallpan id
     * @return hallplan
     */
    Hallplan getById(Long id);

    /**
     * save new hallplan.
     *
     * @param id id of location
     * @param hallplan hallplan to save
     * @return hallplan with saved id
     */
    Hallplan save(Long id, Hallplan hallplan);

    /**
     * edit hallplan.
     *
     * @param hallplan hallplan to edit
     * @return edited hallplan
     */
    Hallplan edit(Hallplan hallplan);

    /**
     * delete Hallplan.
     *
     * @param id hallplan id
     */
    void delete(Long id);
}
