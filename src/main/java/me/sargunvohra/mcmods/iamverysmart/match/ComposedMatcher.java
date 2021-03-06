package me.sargunvohra.mcmods.iamverysmart.match;

import net.minecraft.util.Identifier;

public class ComposedMatcher {
    private SingleMatcher includes;
    private SingleMatcher excludes;

    public ComposedMatcher(SingleMatcher includes, SingleMatcher excludes) {
        this.includes = includes;
        this.excludes = excludes;
    }

    public boolean match(Identifier id) {
        MatchResult excludesMatch = excludes.match(id);
        if (excludesMatch == MatchResult.EXACT) return false;
        MatchResult includesMatch = includes.match(id);
        if (includesMatch == MatchResult.EXACT) return true;
        if (excludesMatch == MatchResult.REGEX) return false;
        return includesMatch == MatchResult.REGEX;
    }

    @Override
    public String toString() {
        return "ComposedMatcher{" + "includes=" + includes + ", excludes=" + excludes + '}';
    }
}
