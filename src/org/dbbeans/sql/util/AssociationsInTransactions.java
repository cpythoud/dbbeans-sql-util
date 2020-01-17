package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBQuerySetup;
import org.dbbeans.sql.DBTransaction;

import java.sql.ResultSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class AssociationsInTransactions {

    public static boolean hasItem(
            final String pairingTable,
            final String field,
            final long idBean,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                "SELECT " + field + " FROM " + pairingTable + " WHERE " + field + "=?",
                stat -> stat.setLong(1, idBean),
                ResultSet::next
        );
    }

    public static int getItemCount(
            final String pairingTable,
            final String field,
            final long idBean,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                "SELECT COUNT(" + field + ") FROM " + pairingTable + " WHERE " + field + "=?",
                stat -> stat.setLong(1, idBean),
                rs -> {
                    rs.next();
                    return rs.getInt(1);
                }
        );
    }

    public static boolean arePaired(
            final String pairingTable,
            final String field1,
            final String field2,
            final long idBean1,
            final long idBean2,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                "SELECT " + field1 + " FROM " + pairingTable + " WHERE " + field1 + "=? AND " + field2 + "=?",
                stat -> {
                    stat.setLong(1, idBean1);
                    stat.setLong(2, idBean2);
                },
                ResultSet::next
        );
    }

    public static boolean arePaired(
            final String pairingTable,
            final String field1,
            final String field2,
            final DbBeanInterface bean1,
            final DbBeanInterface bean2,
            final DBTransaction transaction)
    {
        return arePaired(pairingTable, field1, field2, bean1.getId(), bean2.getId(), transaction);
    }

    private static String getAssociationQuery(final String table, final String field1, final String field2) {
        return "REPLACE INTO " + table + " (" + field1 + ", " + field2 + ") VALUES (?, ?)";
    }

    public static void createAssociation(
            final String table,
            final String field1,
            final String field2,
            final long id1,
            final long id2,
            final DBTransaction transaction)
    {
        transaction.addUpdate(
                getAssociationQuery(table, field1, field2),
                stat -> {
                    stat.setLong(1, id1);
                    stat.setLong(2, id2);
                }
        );
    }

    public static void createAssociation(
            final String table,
            final String field1,
            final String field2,
            final DbBeanInterface bean1,
            final DbBeanInterface bean2,
            final DBTransaction transaction)
    {
        createAssociation(table, field1, field2, bean1.getId(), bean2.getId(), transaction);
    }

    private static String getDissociationQuery(final String table, final String field1, final String field2) {
        return "DELETE FROM " + table + " WHERE " + field1 + "=? AND " + field2 + "=?";
    }

    public static void removeAssociation(
            final String table,
            final String field1,
            final String field2,
            final long id1,
            final long id2,
            final DBTransaction transaction)
    {
        transaction.addUpdate(
                getDissociationQuery(table, field1, field2),
                stat -> {
                    stat.setLong(1, id1);
                    stat.setLong(2, id2);
                }
        );
    }

    public static void removeAssociation(
            final String table,
            final String field1,
            final String field2,
            final DbBeanInterface bean1,
            final DbBeanInterface bean2,
            final DBTransaction transaction)
    {
        removeAssociation(table, field1, field2, bean1.getId(), bean2.getId(), transaction);
    }

    public static boolean associationExists(
            final String table,
            final String field,
            final long id,
            final DBTransaction transaction)
    {
        return transaction.addQuery(
                "SELECT " + field + " FROM " + table + " WHERE " + field + "=?",
                stat -> stat.setLong(1, id),
                ResultSet::next
        );
    }

    public static boolean associationExists(
            final String table,
            final String field,
            final DbBeanInterface bean,
            final DBTransaction transaction)
    {
        return associationExists(table, field, bean.getId(), transaction);
    }

    public static <T extends DbBeanInterface, A extends DbBeanInterface> Optional<A> getAssociatedBean(
            final String table,
            final String referenceIdField,
            final T referencedBean,
            final A returnedBean,
            final DBTransaction transaction)
    {
        return getAssociatedBean(table, referenceIdField, referencedBean.getId(), returnedBean, transaction);
    }

    public static <A extends DbBeanInterface> Optional<A> getAssociatedBean(
            final String table,
            final String referenceIdField,
            final long idReferencedBean,
            final A returnedBean,
            final DBTransaction transaction)
    {
        return getAssociatedBean(
                "SELECT id FROM " + table + " WHERE " + referenceIdField + "=?",
                stat -> stat.setLong(1, idReferencedBean),
                returnedBean,
                transaction
        );
    }

    public static <A extends DbBeanInterface> Optional<A> getAssociatedBean(
            final String query,
            final DBQuerySetup querySetup,
            final A returnedBean,
            final DBTransaction transaction)
    {
        return BasicTransactionQueries.getUniqueElement(query, querySetup, returnedBean, transaction);
    }

    public static boolean associationExists(
            final Collection<String> tables,
            final String field,
            final long id,
            final DBTransaction transaction)
    {
        for (String table: tables)
            if (associationExists(table, field, id, transaction))
                return true;

        return false;
    }

    public static boolean associationExists(
            final Collection<String> tables,
            final String field,
            final DbBeanInterface bean,
            final DBTransaction transaction)
    {
        return associationExists(tables, field, bean.getId(), transaction);
    }

    public static boolean associationExists(
            final String field,
            final long id,
            final DBTransaction transaction,
            final String... tables)
    {
        return associationExists(Arrays.asList(tables), field, id, transaction);
    }

    public static boolean associationExists(
            final String field,
            final DbBeanInterface bean,
            final DBTransaction transaction,
            final String... tables)
    {
        return associationExists(Arrays.asList(tables), field, bean.getId(), transaction);
    }
}
