package com.enterprisehub.backend;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeSalaryStreamsTest {

    record Employee(String name, String department, double salary) {
    }

    Map<String, Double> getAverageSalaryByDepartment(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingDouble(Employee::salary)
                ));
    }

    @Test
    void calculatesAverageSalaryByDepartment() {
        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 4_000),
                new Employee("Bob", "Engineering", 6_000),
                new Employee("Chloe", "Sales", 3_000)
        );

        Map<String, Double> averages = getAverageSalaryByDepartment(employees);

        assertThat(averages)
                .containsEntry("Engineering", 5_000.0)
                .containsEntry("Sales", 3_000.0);
    }
}
