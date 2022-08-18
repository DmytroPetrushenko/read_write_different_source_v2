package com.knubisoft.utils;

import com.knubisoft.strategy.ReadStrategy;
import com.knubisoft.strategy.Strategy;
import com.knubisoft.strategy.WriteStrategy;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

@Data
public class StrategyUtil {
    private static final String PACKAGE = "com.knubisoft";
    private static StrategyUtil strategyUtil;
    private final List<? extends ReadStrategy> readStrategyList;
    private final List<? extends WriteStrategy> writeStrategyList;

    public StrategyUtil() {
        this.readStrategyList = createStrategyList(ReadStrategy.class);
        this.writeStrategyList = createStrategyList(WriteStrategy.class);
    }

    public static StrategyUtil getStrategyUtil() {
        if (strategyUtil == null) {
            strategyUtil = new StrategyUtil();
        }
        return strategyUtil;
    }

    private static <T extends Strategy> List<? extends T> createStrategyList(Class<T> clazz) {
        Set<Class<? extends T>> classSet = new Reflections(PACKAGE, Scanners.SubTypes)
                .getSubTypesOf(clazz);
        return classSet.stream()
                .map(StrategyUtil::createInstance)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private static <T extends Strategy> T createInstance(Class<T> clazz) {
        return clazz.getConstructor().newInstance();
    }
}
