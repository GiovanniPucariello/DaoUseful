package daouseful.dto;

/**
 *
 * @author Daniel Röhers Moura
 */
public class AliasSearch {
    
    private String column;
    
    private String alias;

    public AliasSearch(String column, String alias) {
        this.column = column;
        this.alias = alias;
    }
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    
    
}
