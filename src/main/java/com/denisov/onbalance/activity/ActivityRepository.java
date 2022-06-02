package com.denisov.onbalance.activity;

import com.denisov.onbalance.auth.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface ActivityRepository extends CrudRepository<ActivityEntity, Long> {
    Iterable<ActivityEntity> findAllByUserId(UserEntity userEntity);

}
