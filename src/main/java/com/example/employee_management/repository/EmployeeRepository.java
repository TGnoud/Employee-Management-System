package com.example.employee_management.repository;

import com.example.employee_management.dto.DepartmentEmployeeCount;
import com.example.employee_management.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);

    @Query("""
            select e from Employee e
            where lower(e.name) like lower(concat('%', :keyword, '%'))
               or lower(e.department.name) like lower(concat('%', :keyword, '%'))
            """)
    List<Employee> searchByNameOrDepartment(@Param("keyword") String keyword);

    @Query("""
            select new com.example.employee_management.dto.DepartmentEmployeeCount(
                e.department.name,
                count(e.id)
            )
            from Employee e
            group by e.department.name
            order by count(e.id) desc
            """)
    List<DepartmentEmployeeCount> countEmployeesByDepartment();
}
