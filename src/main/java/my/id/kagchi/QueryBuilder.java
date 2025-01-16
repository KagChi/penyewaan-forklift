package my.id.kagchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryBuilder {
    private final List<String> columns;
    private final List<String> tables;
    private final List<String> conditions;
    private String query;
    private Integer limit;

    public QueryBuilder() {
        this.columns = new ArrayList<String>();
        this.tables = new ArrayList<String>();
        this.conditions = new ArrayList<String>();
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;  // Set the limit value
        return this;
    }

    public QueryBuilder select(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public QueryBuilder from(String... tables) {
        Collections.addAll(this.tables, tables);
        return this;
    }

    public QueryBuilder where(String... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (int i = 0; i < columns.size(); i++) {
            sb.append(columns.get(i));
            if (i < columns.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(" FROM ");
        for (int i = 0; i < tables.size(); i++) {
            sb.append(tables.get(i));
            if (i < tables.size() - 1) {
                sb.append(", ");
            }
        }

        if (!conditions.isEmpty()) {
            sb.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                sb.append(conditions.get(i));
                if (i < conditions.size() - 1) {
                    sb.append(" AND ");
                }
            }
        }

        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }

        return sb.toString();
    }
}
