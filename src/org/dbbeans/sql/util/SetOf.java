package org.dbbeans.sql.util;

import org.dbbeans.util.Money;
import org.dbbeans.util.MoneyFormat;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.HashSet;
import java.util.Set;

public class SetOf {

    public static Set<Boolean> booleans(final ResultSet rs) throws SQLException {
        final Set<Boolean> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getBoolean(1));

        return set;
    }

    public static Set<Integer> integers(final ResultSet rs) throws SQLException {
        final Set<Integer> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getInt(1));

        return set;
    }

    public static Set<Long> longs(final ResultSet rs) throws SQLException {
        final Set<Long> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getLong(1));

        return set;
    }

    public static Set<String> strings(final ResultSet rs) throws SQLException {
        final Set<String> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getString(1));

        return set;
    }

    public static Set<Date> dates(final ResultSet rs) throws SQLException {
        final Set<Date> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getDate(1));

        return set;
    }

    public static Set<Time> times(final ResultSet rs) throws SQLException {
        final Set<Time> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getTime(1));

        return set;
    }

    public static Set<Timestamp> timestamps(final ResultSet rs) throws SQLException {
        final Set<Timestamp> set = new HashSet<>();

        while (rs.next())
            set.add(rs.getTimestamp(1));

        return set;
    }

    public static Set<Money> monies(final ResultSet rs) throws SQLException {
        return monies(rs, MoneyFormat.getDefault());
    }

    public static Set<Money> monies(final ResultSet rs, final MoneyFormat moneyFormat) throws SQLException {
        final Set<Money> list = new HashSet<>();

        while (rs.next())
            list.add(new Money(rs.getLong(1), moneyFormat));

        return list;
    }

}
