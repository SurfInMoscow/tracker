package ru.vorobyev.tracker.repository.jpa.project;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.project.Backlog;
import ru.vorobyev.tracker.repository.BacklogRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class BacklogJpaRepositoryImpl implements BacklogRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Backlog save(Backlog backlog) {
        if (backlog.isNew()) {
            em.persist(backlog);
            return backlog;
        } else {
            return em.merge(backlog);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(Backlog.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Backlog get(int id) {
        return em.find(Backlog.class, id);
    }

    @Override
    public List<Backlog> getAll() {
        return em.createNamedQuery(Backlog.GET_ALL, Backlog.class)
                .getResultList();
    }

    /*
    Возвращается сущность, которая не под управлением Hibernate.
    Можно фильтровать на уровне запроса Issue из БД, а не из Backlog.
    При большом объеме задач лучше выполнить фильтр через Issue.
    */
    @Override
    public Backlog getWithIssuesByPrioriTy(int id, String priority) {
        Backlog backlog = get(id);
        Backlog filteredBacklog = new Backlog();

        filteredBacklog.getBugs().addAll(backlog.getBugs().stream()
                .filter(bug -> bug.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        filteredBacklog.getEpics().addAll(backlog.getEpics().stream()
                .filter(epic -> epic.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        filteredBacklog.getStories().addAll(backlog.getStories().stream()
                .filter(story -> story.getPriority().name().equals(priority)).collect(Collectors.toSet()));
        filteredBacklog.getTasks().addAll(backlog.getTasks().stream()
                .filter(task -> task.getPriority().name().equals(priority)).collect(Collectors.toSet()));

        return filteredBacklog;
    }

    /*
   Возвращается сущность, которая не под управлением Hibernate.
   Можно фильтровать на уровне запроса Issue из БД, а не из Backlog.
   При большом объеме задач лучше выполнить фильтр через Issue.
   */
    @Override
    public Backlog getWithIssuesBetweenDates(int id, LocalDateTime startDate, LocalDateTime endDate) {
        Backlog backlog = get(id);
        Backlog filteredBacklog = new Backlog();

        filteredBacklog.getBugs().addAll(backlog.getBugs().stream()
                .filter(bug -> (bug.getCreationDate().isEqual(startDate) || bug.getCreationDate().isAfter(startDate))
                            && (bug.getCreationDate().isEqual(endDate) || bug.getCreationDate().isBefore(endDate))).collect(Collectors.toSet()));
        filteredBacklog.getEpics().addAll(backlog.getEpics().stream()
                .filter(epic -> (epic.getCreationDate().isEqual(startDate) || epic.getCreationDate().isAfter(startDate))
                             && (epic.getCreationDate().isEqual(endDate) || epic.getCreationDate().isBefore(endDate))).collect(Collectors.toSet()));
        filteredBacklog.getStories().addAll(backlog.getStories().stream()
                .filter(story -> (story.getCreationDate().isEqual(startDate) || story.getCreationDate().isAfter(startDate))
                              && (story.getCreationDate().isEqual(endDate) || story.getCreationDate().isBefore(endDate))).collect(Collectors.toSet()));
        filteredBacklog.getTasks().addAll(backlog.getTasks().stream()
                .filter(task -> (task.getCreationDate().isEqual(startDate) || task.getCreationDate().isAfter(startDate))
                             && (task.getCreationDate().isEqual(endDate) || task.getCreationDate().isBefore(endDate))).collect(Collectors.toSet()));

        return filteredBacklog;
    }

    /*
   Возвращается сущность, которая не под управлением Hibernate.
   Можно фильтровать на уровне запроса Issue из БД, а не из Backlog.
   При большом объеме задач лучше выполнить фильтр через Issue.
   */
    @Override
    public Backlog getWithIssuesByName(int id, String name) {
        Backlog backlog = get(id);
        Backlog filteredBacklog = new Backlog();

        filteredBacklog.getBugs().addAll(backlog.getBugs().stream()
                .filter(bug -> bug.getName().equals(name)).collect(Collectors.toSet()));
        filteredBacklog.getEpics().addAll(backlog.getEpics().stream()
                .filter(epic -> epic.getName().equals(name)).collect(Collectors.toSet()));
        filteredBacklog.getStories().addAll(backlog.getStories().stream()
                .filter(story -> story.getName().equals(name)).collect(Collectors.toSet()));
        filteredBacklog.getTasks().addAll(backlog.getTasks().stream()
                .filter(task -> task.getName().equals(name)).collect(Collectors.toSet()));

        return filteredBacklog;
    }

    /*
   Возвращается сущность, которая не под управлением Hibernate.
   Можно фильтровать на уровне запроса Issue из БД, а не из Backlog.
   При большом объеме задач лучше выполнить фильтр через Issue.
   */
    @Override
    public Backlog getWithIssuesByExecutor(int id, int executor_id) {
        Backlog backlog = get(id);
        Backlog filteredBacklog = new Backlog();

        filteredBacklog.getBugs().addAll(backlog.getBugs().stream()
                .filter(bug -> bug.getExecutor().getId().equals(executor_id)).collect(Collectors.toSet()));
        filteredBacklog.getEpics().addAll(backlog.getEpics().stream()
                .filter(epic -> epic.getExecutor().getId().equals(executor_id)).collect(Collectors.toSet()));
        filteredBacklog.getStories().addAll(backlog.getStories().stream()
                .filter(story -> story.getExecutor().getId().equals(executor_id)).collect(Collectors.toSet()));
        filteredBacklog.getTasks().addAll(backlog.getTasks().stream()
                .filter(task -> task.getExecutor().getId().equals(executor_id)).collect(Collectors.toSet()));

        return filteredBacklog;
    }

    /*
   Возвращается сущность, которая не под управлением Hibernate.
   Можно фильтровать на уровне запроса Issue из БД, а не из Backlog.
   При большом объеме задач лучше выполнить фильтр через Issue.
   */
    @Override
    public Backlog getWithIssuesByReporter(int id, int reporter_id) {
        Backlog backlog = get(id);
        Backlog filteredBacklog = new Backlog();

        filteredBacklog.getBugs().addAll(backlog.getBugs().stream()
                .filter(bug -> bug.getReporter().getId().equals(reporter_id)).collect(Collectors.toSet()));
        filteredBacklog.getEpics().addAll(backlog.getEpics().stream()
                .filter(epic -> epic.getReporter().getId().equals(reporter_id)).collect(Collectors.toSet()));
        filteredBacklog.getStories().addAll(backlog.getStories().stream()
                .filter(story -> story.getReporter().getId().equals(reporter_id)).collect(Collectors.toSet()));
        filteredBacklog.getTasks().addAll(backlog.getTasks().stream()
                .filter(task -> task.getReporter().getId().equals(reporter_id)).collect(Collectors.toSet()));

        return filteredBacklog;
    }

    public void refresh(Backlog backlog) {
        em.refresh(backlog);
    }
}
