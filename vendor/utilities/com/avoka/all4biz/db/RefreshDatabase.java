package com.avoka.all4biz.db;

import javax.sql.DataSource;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.DbGenerator;
import org.apache.cayenne.conf.Configuration;
import org.apache.cayenne.conf.DefaultConfiguration;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.dba.sqlserver.SQLServerAdapter;
import org.apache.cayenne.map.DataMap;


public class RefreshDatabase {

    public static void main(String[] args) {
        DefaultConfiguration c = new DefaultConfiguration("cayenne.xml");
        Configuration.initializeSharedConfiguration(c);
        DbAdapter adapt = null;
        try {
            adapt = SQLServerAdapter.class.newInstance();
            DataContext dc = DataContext.createDataContext();

            for(Object obj : dc.getEntityResolver().getDataMaps()) {
                DataMap map = (DataMap) obj;
                if("All4BizMap".equals(map.getName())) {
                    DataNode node = dc.getParentDataDomain().lookupDataNode(map);
                    DataSource src = node.getDataSource();
                    DbGenerator dbgen = new DbGenerator(adapt,map);
                    dbgen.setShouldDropTables(true);
                    dbgen.setShouldDropPKSupport(true);
                    dbgen.setShouldCreatePKSupport(true);
                    dbgen.setShouldCreateFKConstraints(false);
                    dbgen.setShouldCreateTables(true);
                    dbgen.runGenerator(src);
                }
            }
            BaseContext.bindThreadObjectContext(dc);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
