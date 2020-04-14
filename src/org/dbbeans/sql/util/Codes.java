package org.dbbeans.sql.util;

import org.beanmaker.util.DbBeanInterface;

import org.dbbeans.sql.DBAccess;

import java.sql.ResultSet;

import java.util.Optional;
import java.util.regex.Pattern;

public class Codes {

    private static final Pattern VALIDATION_PATTERN = Pattern.compile("[\\w+-]{4,20}");

    public static boolean validateFormat(final String code) {
        return VALIDATION_PATTERN.matcher(code).matches();
    }

    public static <T extends DbBeanInterface> T getFromCode(
            final T bean,
            final String table,
            final String code,
            final DBAccess dbAccess)
    {
        final long id = getId(table, code, dbAccess);

        if (id == 0)
            return null;

        bean.setId(id);
        return bean;
    }

    public static <T extends DbBeanInterface> Optional<T> getOptionalFromCode(
            final T bean,
            final String table,
            final String code,
            final DBAccess dbAccess)
    {
        final long id = getId(table, code, dbAccess);

        if (id == 0)
            return Optional.empty();

        bean.setId(id);
        return Optional.of(bean);
    }

    public static long getId(final String table, final String code, final DBAccess dbAccess) {
        return dbAccess.processQuery(
                "SELECT id FROM " + table + " WHERE code=?",
                stat -> stat.setString(1, code),
                rs -> {
                    if (rs.next())
                        return rs.getLong(1);

                    return 0L;
                }
        );
    }

    public static boolean isPresent(final String table, final String code, final DBAccess dbAccess) {
        return dbAccess.processQuery(
                "SELECT id FROM " + table + " WHERE code=?",
                stat -> stat.setString(1, code),
                ResultSet::next
        );
    }

}
