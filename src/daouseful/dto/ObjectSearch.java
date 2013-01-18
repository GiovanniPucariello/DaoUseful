package daouseful.dto;

import java.io.Serializable;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;

/**
 *
 * @author Daniel Röhers Moura
 */
public class ObjectSearch<T extends Serializable> {

    private Integer accessPage;
    
    private Integer pageSize;
    
    /**
     * Column to search order for sql
     */
    private String mainColumn;
    
    /**
     * Sort order of search (ASC or DESC)
     */
    private String order;
    
    /**
     * List of attributes to list
     */
    private ProjectionList projections;
    
    /**
     * List of restrictions for research
     */
    List<Criterion> restrictions;
    
    /**
     * Return to class
     */
    private Class<T> classReturn;
    
    /**
     * List alias for research
     */
    private List<AliasSearch> alias;
    
    public ObjectSearch(Integer accessPage, Integer pageSize, String order,
            String mainColumn, ProjectionList projections, List<Criterion> restrictions,
            Class<T> classReturn, List<AliasSearch> alias) {
        this.accessPage = accessPage;
        this.pageSize = pageSize;
        this.mainColumn = mainColumn;
        this.order = order;
        this.projections = projections;
        this.restrictions = restrictions;
        this.classReturn = classReturn;
        this.alias = alias;
    }

    public Integer getAccessPage() {
        return accessPage;
    }

    public void setAccessPage(Integer accessPage) {
        this.accessPage = accessPage;
    }

    public List<AliasSearch> getAlias() {
        return alias;
    }

    public void setAlias(List<AliasSearch> alias) {
        this.alias = alias;
    }

    public String getMainColumn() {
        return mainColumn;
    }

    public void setMainColumn(String mainColumn) {
        this.mainColumn = mainColumn;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public ProjectionList getProjections() {
        return projections;
    }

    public void setProjections(ProjectionList projections) {
        this.projections = projections;
    }

    public List<Criterion> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<Criterion> restrictions) {
        this.restrictions = restrictions;
    }

    public Class<T> getClassReturn() {
        return classReturn;
    }

    public void setClassReturn(Class<T> classReturn) {
        this.classReturn = classReturn;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}