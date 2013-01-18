package daouseful.dao.cfg;

import daouseful.dto.AliasSearch;
import daouseful.dto.ObjectSearch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Daniel Röhers Moura
 */
public abstract class BaseDao<T extends Serializable, ID extends Serializable> extends GenericDaoImp<T, ID> {
    
        
    public List listing(ObjectSearch objectSearch) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);
            
            if (objectSearch.getAlias() != null) {
                for (Iterator it = objectSearch.getAlias().iterator(); it.hasNext();) {
                    AliasSearch alias = (AliasSearch) it.next();
                    criteria.createAlias(alias.getColumn(), alias.getAlias(), Criteria.LEFT_JOIN);
                }
            }
            
            if (objectSearch.getProjections() != null) {
                criteria.setProjection(objectSearch.getProjections());
            }
            
            if (objectSearch.getRestrictions() != null ) {
                for (Iterator it = objectSearch.getRestrictions().iterator(); it.hasNext();) {
                    criteria.add((Criterion) it.next());
                }
            }
            setPageProperties(objectSearch, criteria);
            criteria.setResultTransformer(Transformers.aliasToBean(objectSearch.getClassReturn()));
            List lista = criteria.list();
            closingSession();
            return lista;
        } catch (Exception ex) {
            closingSession();
            return new ArrayList();
        }

    }
    
    private void setPageProperties(ObjectSearch objectSearch, Criteria criteria) {
        if (StringUtils.isNotEmpty(objectSearch.getMainColumn()) 
                && StringUtils.isNotEmpty(objectSearch.getOrder())) {
            criteria.addOrder("desc".equalsIgnoreCase(objectSearch.getOrder()) 
                    ? Order.desc(objectSearch.getMainColumn()) 
                    : Order.asc(objectSearch.getMainColumn()));
        }
        
        // define paginação
        Integer primeiroRegistro = (objectSearch.getPageSize() * (objectSearch.getAccessPage() - 1));
        criteria.setFirstResult(primeiroRegistro);
        criteria.setMaxResults(objectSearch.getPageSize());
        
    }
    
    public Integer totalRecordsOfListing(ObjectSearch objectSearch) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);
            if (objectSearch.getAlias() != null) {
                for (Iterator it = objectSearch.getAlias().iterator(); it.hasNext();) {
                    AliasSearch alias = (AliasSearch) it.next();
                    criteria.createAlias(alias.getColumn(), alias.getAlias(), Criteria.LEFT_JOIN);
                }
            }
            if (objectSearch.getRestrictions() != null ) {
                for (Iterator it = objectSearch.getRestrictions().iterator(); it.hasNext();) {
                    criteria.add((Criterion) it.next());
                }
            }
            criteria.setProjection(Projections.rowCount());
            Integer qtde = (Integer) criteria.uniqueResult();
            definePageAccessed(qtde,objectSearch);
            closingSession();
            return qtde;
        } catch (Exception e) {
            definePageAccessed(0,objectSearch);
            closingSession();
            return 0;
        }
    }
    
    public Integer calculatesTheTotalPage(Integer totalRecords, Integer recordsPerPage) {
         return (totalRecords % recordsPerPage == 0) 
                 ? (totalRecords / recordsPerPage) : (totalRecords / recordsPerPage) + 1;            
    }
    
    private void definePageAccessed(Integer totalRecords, ObjectSearch objetoSearch){
        if (totalRecords != null && objetoSearch.getPageSize() != null && totalRecords < objetoSearch.getPageSize()){
            objetoSearch.setAccessPage(1);
        }
    }
    
}
