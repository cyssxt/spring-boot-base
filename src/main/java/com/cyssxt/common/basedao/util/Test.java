package com.cyssxt.common.basedao.util;

import java.sql.SQLException;
import java.sql.Wrapper;

public class Test implements Wrapper {
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
