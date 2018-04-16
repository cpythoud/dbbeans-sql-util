package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBAccess;
import org.dbbeans.sql.DBQuerySetup;

import java.util.Optional;

public class BasicQueries {

    public static <B extends DbBeanInterface> Optional<B> getUniqueElement(
            final String query,
            final DBQuerySetup querySetup,
            final B returnedBean,
            final DBAccess dbAccess)
    {
        return Optional.ofNullable(
                dbAccess.processQuery(
                        query,
                        querySetup,
                        rs -> {
                            int count = 0;
                            while (rs.next()) {
                                returnedBean.setId(rs.getLong(1));
                                ++count;
                            }

                            if (count == 0)
                                return null;

                            if (count == 1)
                                return returnedBean;

                            throw new IllegalStateException("Too many results: " + count);
                        }
                )
        );
    }

    public static long getUniqueID(
            final String query,
            final DBQuerySetup querySetup,
            final DBAccess dbAccess)
    {
        return dbAccess.processQuery(
                query,
                querySetup,
                rs -> {
                    long id = 0;
                    int count = 0;
                    while (rs.next()) {
                        id = rs.getLong(1);
                        ++count;
                    }

                    if (count > 1)
                        throw new IllegalStateException("Too many results: " + count);

                    return id;
                }
        );
    }
}
