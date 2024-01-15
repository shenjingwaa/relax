package com.relax.relax.common.proxy.node.conversion;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.enums.SqlType;
import com.relax.relax.common.proxy.RelaxViewBeforeProxy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Objects;

public class DefaultInfoConversionProxyNode extends RelaxViewBeforeProxy {

    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        String uniqueFieldName = SqlType.getUniqueFieldName(t);
        String parameter = request.getParameter(uniqueFieldName);
        Assert.isTrue(Objects.nonNull(parameter) && !parameter.isEmpty(), "唯一标识不能为空!");

        try {
            Field field = getExecutor().getBaseEntityClass().getDeclaredField(uniqueFieldName);
            field.setAccessible(true);
            if (Objects.equals(field.getType(), String.class)) {
                field.set(t, parameter);
            } else if (Objects.equals(field.getType(), Long.class)) {
                field.set(t, Long.parseLong(parameter));
            } else if (Objects.equals(field.getType(), Integer.class)) {
                field.set(t, Integer.parseInt(parameter));
            } else if (Objects.equals(field.getType(), Short.class)) {
                field.set(t, Short.parseShort(parameter));
            } else {
                throw new IllegalArgumentException("args format error.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("args format error.");
        }
        return t;
    }

    @Override
    public <T> boolean check(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.INFO;
    }
}
