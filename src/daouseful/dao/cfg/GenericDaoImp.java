package daouseful.dao.cfg;

import daouseful.useful.Useful;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Daniel Röhers Moura
 */
public class GenericDaoImp<T, ID extends Serializable> implements GenericDao<T, ID> {

    protected Session session = null;
    protected Class<T> persistentClass = (Class<T>) DaoUseful.getTypeArguments(GenericDaoImp.class, this.getClass()).get(0);
    private boolean transaction;

    /**
     * Start criteria
     * @return 
     */
    public Criteria startCriteria() {
        Criteria criteria = session.createCriteria(persistentClass, "tab");
        return criteria;
    }

    /**
     * Get session
     */
    public void getSession() {
        session = HibernateUseful.getSessionFactory(false).openSession();
    }

    /**
     * Close session
     */
    public void closingSession() {
        session.close();
        transaction = false;
    }

    /**
     * Start transaction
     */
    public void startTransaction() {
        session.beginTransaction();
        transaction = true;
    }

    /**
     * Realize commit
     */
    public void realizeCommit() {
        session.getTransaction().commit();
    }

    /**
     * Realize roll back
     */
    public void realizeRollback() {
        session.getTransaction().rollback();
    }

    /**
     * Find object by id
     * @param id
     * @return 
     */
    @Override
    public T findById(ID id) {
        try {
            getSession();
            T t = (T) session.get(persistentClass, id);
            closingSession();
            return t;
        } catch (HibernateException e) {
            treatException(e);
            closingSession();
            return (T) new Object();
        } catch (Exception e) {
            treatException(e);
            return (T) new Object();
        }

    }

    /**
     * Find all objects
     * @param fieldOrder
     * @return 
     */
    @Override
    public List<T> findAll(String fieldOrder) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);
            if (StringUtils.isNotEmpty(fieldOrder)) {
                criteria.addOrder(Order.asc(fieldOrder));
            }
            List<T> list = criteria.list();
            closingSession();
            return list;
        } catch (HibernateException e) {
            treatException(e);
            closingSession();
            return new ArrayList<T>();
        } catch (Exception e) {
            treatException(e);
            return new ArrayList<T>();
        }
    }

    /**
     * Find all objects by sql
     * @param sql
     * @param fieldOrder
     * @return 
     */
    @Override
    public List<T> findBySql(String sql, String fieldOrder) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);
            criteria.add(Restrictions.sqlRestriction(sql));
            if (StringUtils.isNotEmpty(fieldOrder)) {
                criteria.addOrder(Order.asc(fieldOrder));
            }
            List list = criteria.list();
            closingSession();
            return list;
        } catch (HibernateException e) {
            treatException(e);
            return new ArrayList<T>();
        } catch (Exception e) {
            treatException(e);
            return new ArrayList<T>();
        }
    }

    /**
     * Save object
     * @param entity
     * @return 
     */
    @Override
    public boolean save(T entity) {
        try {
            getSession();
            startTransaction();
            session.save(entity);
            realizeCommit();
            closingSession();
            return true;
        } catch (HibernateException e) {
            treatException(e);
            return false;
        } catch (Exception e) {
            treatException(e);
            return false;
        }
    }

    /**
     * Update object
     * @param entity
     * @return 
     */
    @Override
    public boolean update(T entity) {
        try {
            getSession();
            startTransaction();
            session.update(entity);
            realizeCommit();
            closingSession();
            return true;
        } catch (HibernateException e) {
            treatException(e);
            return false;
        } catch (Exception e) {
            treatException(e);
            return false;
        }
    }

    /**
     * Save or update object
     * @param entity
     * @return 
     */
    @Override
    public boolean saveOrUpdate(T entity) {
        try {
            getSession();
            startTransaction();
            session.saveOrUpdate(entity);
            realizeCommit();
            closingSession();
            return true;
        } catch (HibernateException e) {
            treatException(e);
            return false;
        } catch (Exception e) {
            treatException(e);
            return false;
        }
    }

    /**
     * Delete object
     * @param entity
     * @return 
     */
    @Override
    public boolean delete(T entity) {
        try {
            getSession();
            startTransaction();
            session.delete(entity);
            realizeCommit();
            closingSession();
            return true;
        } catch (HibernateException e) {
            treatException(e);
            return false;
        } catch (Exception e) {
            treatException(e);
            return false;
        }
    }

    /**
     * Returns the largest value of the attribute
     * @param attribute
     * @return 
     */
    @Override
    public Long max(String attribute) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setProjection(Projections.max(attribute));
            Long max = (Long) criteria.uniqueResult();
            closingSession();
            return max == null ? 0L : max;
        } catch (Exception e) {
            treatException(e);
            return 0L;
        }
    }
    
    /**
     * Treat execption:
     * Rollback if any transaction
     * Close session
     * @param e 
     */
    private void treatException(Exception e) {
        if(transaction) realizeRollback();
        closingSession();
        Useful.exceptionMessageConsole(e);
    }
}
