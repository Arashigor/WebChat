package dao;

import model.User;

import javax.persistence.*;

public class UserDaoImp {

    public boolean persist(User entity) {
        boolean persisted = true;

        EntityManager entityManager = EntityManagerProvider.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
        } catch (EntityExistsException e) {
            persisted = false;
        } finally {
            entityManager.getTransaction().commit();
        }

        return persisted;
    }

    //TODO redo as strings and normal exc handler
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
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return requestedUser != null;
    }

}
