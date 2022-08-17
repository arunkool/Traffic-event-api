package com.sensys.event.repository;

import com.sensys.event.entity.Violation;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationRepository extends CrudRepository<Violation, String> {

 /*   @ContinuousQuery(query = "SELECT * FROM /Violations a WHERE a.paid = true")
    public List<String> getPaidAmountList();

    @ContinuousQuery(query = "SELECT * FROM /Violations a WHERE a.paid = false")
    public List<String> getToBePaidAmountList();*/
}
