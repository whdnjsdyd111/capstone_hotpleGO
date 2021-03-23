package com.example.demo.api;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

@Component
public class MyHotpleAPI extends AbstractDialect implements IExpressionObjectDialect {
    protected MyHotpleAPI() {
        super("hotpleAPI");
    }

    @Override

    public IExpressionObjectFactory getExpressionObjectFactory() {

        return new IExpressionObjectFactory() {
            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("hotpleAPI");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new HotpleAPI();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}
