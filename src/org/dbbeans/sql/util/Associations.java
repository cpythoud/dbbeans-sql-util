package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBAccess;

import java.sql.ResultSet;

public class Associations {

    public static boolean hasItem(
            final String pairingTable,
            final String field,
            final long idBean,
            final DBAccess dbAccess)
    {
        return dbAccess.processQuery(
                "SELECT " + field + " FROM " + pairingTable + " WHERE " + field + "=?",
                stat -> stat.setLong(1, idBean),
                ResultSet::next
        );
    }

    public static int getItemCount(
            final String pairingTable,
            final String field,
            final long idBean,
            final DBAccess dbAccess)
    {
        return dbAccess.processQuery(
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
            final DBAccess dbAccess)
    {
        return dbAccess.processQuery(
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
            final DBAccess dbAccess)
    {
        return arePaired(pairingTable, field1, field2, bean1.getId(), bean2.getId(), dbAccess);
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
            final DBAccess dbAccess)
    {
        dbAccess.processUpdate(
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
            final DBAccess dbAccess)
    {
        createAssociation(table, field1, field2, bean1.getId(), bean2.getId(), dbAccess);
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
            final DBAccess dbAccess)
    {
        dbAccess.processUpdate(
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
            final DBAccess dbAccess)
    {
        removeAssociation(table, field1, field2, bean1.getId(), bean2.getId(), dbAccess);
    }

    public static boolean associationExists(
            final String table,
            final String field,
            final long id,
            final DBAccess dbAccess)
    {
        return dbAccess.processQuery(
                "SELECT " + field + " FROM " + table + " WHERE " + field + "=?",
                stat -> stat.setLong(1, id),
                ResultSet::next
        );
    }

    public static boolean associationExists(
            final String table,
            final String field,
            final DbBeanInterface bean,
            final DBAccess dbAccess)
    {
        return associationExists(table, field, bean.getId(), dbAccess);
    }
}
