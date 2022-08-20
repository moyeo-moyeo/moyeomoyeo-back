package com.back.moyeomoyeo.repository.promise;

import com.back.moyeomoyeo.entity.promise.Promise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRepository extends JpaRepository<Promise, Long> {
}
