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

    public Criteria startCriteria() {
        Criteria cri = session.createCriteria(persistentClass, "tab");
        return cri;
    }

    public void getSession() {
        session = HibernateUseful.getSessionFactory(false).openSession();
    }

    public void closingSession() {
        session.close();
        transaction = false;
    }

    public void startTransaction() {
        session.beginTransaction();
        transaction = true;
    }

    public void realizeCommit() {
        session.getTransaction().commit();
    }

    public void realizeRollback() {
        session.getTransaction().rollback();
    }

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

    @Override
    public List<T> findAll(String fieldOrder) {
        try {
            getSession();
            Criteria cri = startCriteria();
            cri.setCacheMode(CacheMode.IGNORE);
            if (StringUtils.isNotEmpty(fieldOrder)) {
                cri.addOrder(Order.asc(fieldOrder));
            }
            List<T> lista = cri.list();
            closingSession();
            return lista;
        } catch (HibernateException e) {
            treatException(e);
            closingSession();
            return new ArrayList<T>();
        } catch (Exception e) {
            treatException(e);
            return new ArrayList<T>();
        }
    }

    @Override
    public List<T> findBySql(String sql, String fieldOrder) {
        try {
            getSession();
            Criteria cri = startCriteria();
            cri.setCacheMode(CacheMode.IGNORE);
            cri.add(Restrictions.sqlRestriction(sql));
            if (StringUtils.isNotEmpty(fieldOrder)) {
                cri.addOrder(Order.asc(fieldOrder));
            }
            List lista = cri.list();
            closingSession();
            return lista;
        } catch (HibernateException e) {
            treatException(e);
            return new ArrayList<T>();
        } catch (Exception e) {
            treatException(e);
            return new ArrayList<T>();
        }
    }

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
    
    private void treatException(Exception e) {
        if(transaction) realizeRollback();
        closingSession();
        Useful.exceptionMessageConsole(e);
    }
}
