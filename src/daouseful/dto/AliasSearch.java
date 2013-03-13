package daouseful.dto;

/**
 *
 * @author Daniel Röhers Moura
 */
public class AliasSearch {

    private String column;
    private String alias;

    /**
     * 
     * @param column
     * @param alias 
     */
    public AliasSearch(String column, String alias) {
        this.column = column;
        this.alias = alias;
    }

    /**
     * 
     * @return 
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 
     * @param alias 
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 
     * @return 
     */
    public String getColumn() {
        return column;
    }

    /**
     * 
     * @param column 
     */
    public void setColumn(String column) {
        this.column = column;
    }
}
