package org.dbbeans.sql.util;

import org.dbbeans.util.Money;
import org.dbbeans.util.MoneyFormat;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ListOf {

    public static List<Boolean> booleans(final ResultSet rs) throws SQLException {
        final List<Boolean> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getBoolean(1));

        return list;
    }

    public static List<Integer> integers(final ResultSet rs) throws SQLException {
        final List<Integer> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getInt(1));

        return list;
    }

    public static List<Long> longs(final ResultSet rs) throws SQLException {
        final List<Long> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getLong(1));

        return list;
    }

    public static List<String> strings(final ResultSet rs) throws SQLException {
        final List<String> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getString(1));

        return list;
    }

    public static List<Date> dates(final ResultSet rs) throws SQLException {
        final List<Date> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getDate(1));

        return list;
    }

    public static List<Time> times(final ResultSet rs) throws SQLException {
        final List<Time> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getTime(1));

        return list;
    }

    public static List<Timestamp> timestamps(final ResultSet rs) throws SQLException {
        final List<Timestamp> list = new ArrayList<>();

        while (rs.next())
            list.add(rs.getTimestamp(1));

        return list;
    }

    public static List<Money> monies(final ResultSet rs) throws SQLException {
        return monies(rs, MoneyFormat.getDefault());
    }

    public static List<Money> monies(final ResultSet rs, final MoneyFormat moneyFormat) throws SQLException {
        final List<Money> list = new ArrayList<>();

        while (rs.next())
            list.add(new Money(rs.getLong(1), moneyFormat));

        return list;
    }
}
