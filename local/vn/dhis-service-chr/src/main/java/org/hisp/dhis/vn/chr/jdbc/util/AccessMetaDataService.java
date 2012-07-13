package org.hisp.dhis.vn.chr.jdbc.util;

import java.sql.SQLException;
import java.util.Set;

public interface AccessMetaDataService
{

    /**
     * Get All tables in database
     * 
     * @return List of table names
     */
    public Set<String> getAllTablesName();

    /**
     * Check table is exist or no
     * 
     * @return exist or no
     */
    public boolean existTable( String tablename )
        throws SQLException;

    /**
     * Get All columns in a table
     * 
     * @return List of column names
     */
    public Set<String> getAllColumnsOfTable( String tableName );
}
