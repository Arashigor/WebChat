package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

class EntityManagerProvider {

    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("WebChatUsers");
        } catch (PersistenceException e) {
            e.printStackTrace();
            entityManagerFactory.close();
        }
    }

    static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
