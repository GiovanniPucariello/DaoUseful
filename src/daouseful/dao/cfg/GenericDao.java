package daouseful.dao.cfg;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Daniel Röhers Moura
 */
public interface GenericDao<T, ID extends Serializable> {

    public T findById(ID id);
    
    public List<T> findAll(String fieldOrder);
    
    public List<T> findBySql(String sql, String fieldOrder);
    
    public Long max(String field);
    
    public boolean save(T entity);
    
    public boolean update(T entity);
    
    public boolean saveOrUpdate(T entity);
    
    public boolean delete(T entity);
                 
}
