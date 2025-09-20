package be.ecam.basics.exercises;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Catalog {
    public static class Product {
        private final String name;
        private final List<String> tags;

        public Product(String name, List<String> tags) {
            this.name = name;
            this.tags = tags;
        }

        public String getName() { return name; }
        public List<String> getTags() { return tags; }
    }

    public static int countTags(@NotNull Product p) {
        List<String> tags = p.getTags();
        return (tags == null) ? 0 : tags.size();
    }
}
