package ru.vorobyev.tracker.repository.jpa.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vorobyev.tracker.domain.user.User;
import ru.vorobyev.tracker.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("jpa")
@Repository
@Transactional(readOnly = true)
public class UserJpaRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public User save(User user) {
        if (user.isNew()) {
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public User get(int id) {
        return em.find(User.class, id);
       /* List<User> users = em.createNamedQuery(User.GET, User.class)
                .setParameter("id", id)
                .getResultList();
        return DataAccessUtils.singleResult(users);*/
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = em.createNamedQuery(User.BY_EMAIL, User.class)
                .setParameter("email", email)
                .getResultList();
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.GET_ALL, User.class)
                .getResultList();
    }

    public void refresh(User user) {
        em.refresh(user);
    }
}
