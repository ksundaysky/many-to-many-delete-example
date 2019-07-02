import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        H2Conection.getSessionFactory().getCurrentSession();

        Session session=H2Conection.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // Dodawanie dwoch pracownikow oraz dwoch projektów obu pracownikow jest w obu projektach
        String[] employeeData = { "Peter Oven", "Allan Norman" };
        String[] projectData = { "IT Project", "Networking Project" };
        Set<Project> projects = new HashSet<>();

        for (String proj : projectData) {
            projects.add(new Project(proj));
        }

        for (String emp : employeeData) {
            Employee employee = new Employee(emp.split(" ")[0],
                    emp.split(" ")[1]);

            employee.setProjects(projects);

            session.persist(employee);
        }
        transaction.commit();

        // Usunięcie projektu o id 1

        session=H2Conection.getSessionFactory().getCurrentSession();
        transaction = session.beginTransaction();


        Project project = session.get(Project.class,1l);

        // usuniecie ze zbioru projektow pracowników projektu o id 1, co daje nam usuniecie z tablei łaczacej wszedzie gdzie  projekt_id=1
        for(Employee e : project.getEmployees())
        {
            e.getProjects().remove(project);
        }

        // Dopiero teraz mozemy usunac bezpiecznie projekt
        session.remove(project);
        transaction.commit();
    }
}
