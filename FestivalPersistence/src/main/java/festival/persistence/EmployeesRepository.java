package festival.persistence;


import festival.model.Employee;

public interface EmployeesRepository extends Repository<Long, Employee> {
    Employee findOneEmployee(String email);
}
