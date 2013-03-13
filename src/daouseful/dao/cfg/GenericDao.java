package daouseful.dao.cfg;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Daniel Röhers Moura
 */
public interface GenericDao<T, ID extends Serializable> {

    /**
     * 
     * @param id
     * @return 
     */
    public T findById(ID id);
    
    /**
     * 
     * @param fieldOrder
     * @return 
     */
    public List<T> findAll(String fieldOrder);
    
    /**
     * 
     * @param sql
     * @param fieldOrder
     * @return 
     */
    public List<T> findBySql(String sql, String fieldOrder);
    
    /**
     * 
     * @param field
     * @return 
     */
    public Long max(String field);
    
    /**
     * 
     * @param entity
     * @return 
     */
    public boolean save(T entity);
    
    /**
     * 
     * @param entity
     * @return 
     */
    public boolean update(T entity);
    
    /**
     * 
     * @param entity
     * @return 
     */
    public boolean saveOrUpdate(T entity);
    
    /**
     * 
     * @param entity
     * @return 
     */
    public boolean delete(T entity);
                 
}
