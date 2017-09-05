package dao;

import model.User;

import javax.persistence.*;

public class UserDao {

    public boolean persist(User entity) {
        boolean persisted = true;

        EntityManager entityManager = null;
        try {
            entityManager = EntityManagerProvider.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            persisted = false;
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return persisted;
    }

    public boolean find(User user) {
        EntityManager entityManager = null;
        User requestedUser = null;
        try {
            entityManager = EntityManagerProvider.getEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager
                    .createQuery("from User where login=:login and password=:password");
            query.setParameter("login",user.getLogin());
            query.setParameter("password",user.getPassword());

            requestedUser = (User) query.getSingleResult();

            entityManager.getTransaction().commit();

        } catch (PersistenceException e) {
            if (entityManager != null) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return requestedUser != null;
    }

}
