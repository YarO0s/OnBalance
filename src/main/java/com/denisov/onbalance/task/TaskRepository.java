package com.denisov.onbalance.task;

import com.denisov.onbalance.activity.ActivityEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    public Iterable<TaskEntity> findAllByActivityId(ActivityEntity activity);

    @Modifying
    @Transactional
    @Query(value="UPDATE task SET title = ?2, description = ?3, completion_status = ?4 where id = ?1", nativeQuery = true)
    public int update(long id, String title, String description, boolean status);
}
