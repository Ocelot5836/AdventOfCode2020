package io.github.ocelot;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AirportBag
{
    private final String identifier;
    private final int count;
    private final Set<AirportBag> children;

    public AirportBag(String identifier, int count)
    {
        this.identifier = identifier;
        this.count = count;
        this.children = new HashSet<>();
    }

    public void addChild(AirportBag bag)
    {
        this.children.add(bag);
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public int getCount()
    {
        return count;
    }

    public Set<AirportBag> getChildren()
    {
        return children;
    }

    @Override
    public String toString()
    {
        return "AirportBag{" +
                "identifier='" + identifier + '\'' +
                ", count=" + count +
                ", children=" + children +
                '}';
    }
}
