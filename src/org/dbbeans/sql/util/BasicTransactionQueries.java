package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBQueryRetrieveData;
import org.dbbeans.sql.DBQuerySetup;
import org.dbbeans.sql.DBTransaction;

import java.util.Optional;

import java.util.function.Consumer;
import java.util.function.Function;

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

    public static void wrap(Consumer<DBTransaction> transactedFunction, DBTransaction transaction) {
        wrap(transactedFunction, transaction, null);
    }

    public static void wrap(
            Consumer<DBTransaction> transactedFunction,
            DBTransaction transaction,
            Consumer<Throwable> errorProcessor)
    {
        try {
            transactedFunction.accept(transaction);
        } catch (RuntimeException rtex) {
            transaction.rollback();
            if (errorProcessor != null)
                errorProcessor.accept(rtex);
            throw rtex;
        } catch (Exception ex) {
            transaction.rollback();
            if (errorProcessor != null)
                errorProcessor.accept(ex);
            throw new RuntimeException(ex);
        }

        transaction.commit();
    }

    public static <B extends DbBeanInterface> B wrap(Function<DBTransaction, B> transactedGetter, DBTransaction transaction) {
        return wrap(transactedGetter, transaction, null);
    }

    public static <B extends DbBeanInterface> B wrap(
            Function<DBTransaction, B> transactedGetter,
            DBTransaction transaction,
            Consumer<Throwable> errorProcessor)
    {
        B bean;
        try {
            bean = transactedGetter.apply(transaction);
        } catch (RuntimeException rtex) {
            transaction.rollback();
            if (errorProcessor != null)
                errorProcessor.accept(rtex);
            throw rtex;
        } catch (Exception ex) {
            transaction.rollback();
            if (errorProcessor != null)
                errorProcessor.accept(ex);
            throw new RuntimeException(ex);
        }

        transaction.commit();

        return bean;
    }

    public static <B extends DbBeanInterface> B getFromItemOrder(
            final String table,
            final long itemOrder,
            B returnedBean,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                "SELECT id FROM " + table + " WHERE item_order=?",
                stat -> stat.setLong(1, itemOrder),
                rs -> {
                    if (rs.next()) {
                        returnedBean.setId(rs.getLong(1));
                        return returnedBean;
                    }

                    throw new IllegalArgumentException("No bean with item_order #" + itemOrder);
                }
        );
    }
}
