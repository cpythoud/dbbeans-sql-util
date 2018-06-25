package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBQueryRetrieveData;
import org.dbbeans.sql.DBQuerySetup;
import org.dbbeans.sql.DBTransaction;

import java.util.Optional;

public class BasicTransactionQueries {

    public static <B extends DbBeanInterface> Optional<B> getUniqueElement(
            final String query,
            final DBQuerySetup querySetup,
            final B returnedBean,
            final DBTransaction transaction)
    {
        return Optional.ofNullable(
                transaction.addQuery(
                        query,
                        querySetup,
                        rs -> {
                            return BasicQueries.getUniqueBean(returnedBean, rs);
                        }
                )
        );
    }

    public static long getUniqueID(
            final String query,
            final DBQuerySetup querySetup,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                query,
                querySetup,
                (DBQueryRetrieveData<Long>) BasicQueries::getUniqueID
        );
    }
}
