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

    /**
     *
     * @param objectSearch
     * @return
     */
    public List listing(ObjectSearch objectSearch) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);

            if (objectSearch.getAlias() != null) {
                for (Iterator iterator = objectSearch.getAlias().iterator(); iterator.hasNext();) {
                    AliasSearch aliasSearch = (AliasSearch) iterator.next();
                    criteria.createAlias(aliasSearch.getColumn(), aliasSearch.getAlias(), Criteria.LEFT_JOIN);
                }
            }

            if (objectSearch.getProjections() != null) {
                criteria.setProjection(objectSearch.getProjections());
            }

            if (objectSearch.getRestrictions() != null) {
                for (Iterator iterator = objectSearch.getRestrictions().iterator(); iterator.hasNext();) {
                    criteria.add((Criterion) iterator.next());
                }
            }
            setPageProperties(objectSearch, criteria);
            criteria.setResultTransformer(Transformers.aliasToBean(objectSearch.getClassReturn()));
            List list = criteria.list();
            closingSession();
            return list;
        } catch (Exception ex) {
            closingSession();
            return new ArrayList();
        }
    }

    /**
     *
     * @param objectSearch
     * @param criteria
     */
    private void setPageProperties(ObjectSearch objectSearch, Criteria criteria) {
        if (StringUtils.isNotEmpty(objectSearch.getMainColumn())
                && StringUtils.isNotEmpty(objectSearch.getOrder())) {
            criteria.addOrder("desc".equalsIgnoreCase(objectSearch.getOrder())
                    ? Order.desc(objectSearch.getMainColumn())
                    : Order.asc(objectSearch.getMainColumn()));
        }

        // defines paging
        Integer firstRecord = (objectSearch.getPageSize() * (objectSearch.getAccessPage() - 1));
        criteria.setFirstResult(firstRecord);
        criteria.setMaxResults(objectSearch.getPageSize());

    }

    /**
     *
     * @param objectSearch
     * @return
     */
    public Integer totalRecordsOfListing(ObjectSearch objectSearch) {
        try {
            getSession();
            Criteria criteria = startCriteria();
            criteria.setCacheMode(CacheMode.IGNORE);
            if (objectSearch.getAlias() != null) {
                for (Iterator iterator = objectSearch.getAlias().iterator(); iterator.hasNext();) {
                    AliasSearch aliasSearch = (AliasSearch) iterator.next();
                    criteria.createAlias(aliasSearch.getColumn(), aliasSearch.getAlias(), Criteria.LEFT_JOIN);
                }
            }
            if (objectSearch.getRestrictions() != null) {
                for (Iterator it = objectSearch.getRestrictions().iterator(); it.hasNext();) {
                    criteria.add((Criterion) it.next());
                }
            }
            criteria.setProjection(Projections.rowCount());
            Integer amount = (Integer) criteria.uniqueResult();
            definePageAccessed(amount, objectSearch);
            closingSession();
            return amount;
        } catch (Exception e) {
            definePageAccessed(0, objectSearch);
            closingSession();
            return 0;
        }
    }

    /**
     *
     * @param totalRecords
     * @param recordsPerPage
     * @return
     */
    public Integer calculatesTheTotalPage(Integer totalRecords, Integer recordsPerPage) {
        return (totalRecords % recordsPerPage == 0)
                ? (totalRecords / recordsPerPage) : (totalRecords / recordsPerPage) + 1;
    }

    /**
     *
     * @param totalRecords
     * @param objetoSearch
     */
    private void definePageAccessed(Integer totalRecords, ObjectSearch objetoSearch) {
        if (totalRecords != null && objetoSearch.getPageSize() != null && totalRecords < objetoSearch.getPageSize()) {
            objetoSearch.setAccessPage(1);
        }
    }
}
