package org.ikigaidigital.helper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for interest strategies, adhering to Open/Closed Principle
 */
@Component
public class InterestStrategyRegistry {
    private final Map<String, InterestStrategy> strategies;

    public InterestStrategyRegistry(List<InterestStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(InterestStrategy::getPlanCode, Function.identity()));
    }

    /**
     * Calculate interest using registered strategies, with a 30-day guard
     */
    public double interestFor(String planCode, double balance, int days) {
        InterestStrategy strat = strategies.get(planCode.toLowerCase());
        if (strat == null) {
            return 0;
        }
        return strat.calculate(balance, days);
    }
}
