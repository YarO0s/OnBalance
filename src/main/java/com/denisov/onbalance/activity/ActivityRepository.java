package com.denisov.onbalance.activity;

import com.denisov.onbalance.auth.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {
    Iterable<ActivityEntity> findAllByUserId(UserEntity userEntity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ActivityEntity set name = ?1, color =?2, description =?3," +
                   " completion_status=?4 WHERE id = ?5")
    public int update(String title, String color, String description,
                      int completionStatus, long activityId);

    long countByUserId(UserEntity userId);
}
