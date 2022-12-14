package io.github.ocelot.aoc22.util;

public record Pair<L, R>(L left, R right)
{
    static <L, R> Pair<L, R> of(L left, R right)
    {
        return new Pair<>(left, right);
    }
}
