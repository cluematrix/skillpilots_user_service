package com.skilluser.user.repository;

import com.skilluser.user.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {

    @Query("SELECT d.dept_name FROM Department d WHERE d.deptId = :deptId")
    String getDepartmentNameById(@Param("deptId") Long deptId);

}
