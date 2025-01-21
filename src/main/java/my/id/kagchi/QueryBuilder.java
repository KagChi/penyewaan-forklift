package my.id.kagchi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryBuilder {
    private final List<String> columns;
    private final List<String> tables;
    private final List<String> conditions;
    private final List<String> values;
    private String queryType;
    private Integer limit;

    public QueryBuilder() {
        this.columns = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.values = new ArrayList<>();
        this.queryType = "SELECT";
        this.limit = null;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder select(String... columns) {
        this.queryType = "SELECT";
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public QueryBuilder from(String... tables) {
        Collections.addAll(this.tables, tables);
        return this;
    }

    public QueryBuilder to(String... tables) {
        Collections.addAll(this.tables, tables);
        return this;
    }

    public QueryBuilder where(String... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public QueryBuilder update(String... columnsWithValues) {
        this.queryType = "UPDATE";
        Collections.addAll(this.values, columnsWithValues);
        return this;
    }

    public QueryBuilder insert(String... columns) {
        this.queryType = "INSERT";
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public QueryBuilder values(String... values) {
        Collections.addAll(this.values, values);
        return this;
    }

    public QueryBuilder delete() {
        this.queryType = "DELETE";
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();

        switch (queryType) {
            case "SELECT":
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
                break;

            case "UPDATE":
                sb.append("UPDATE ");
                sb.append(tables.get(0));

                sb.append(" SET ");
                for (int i = 0; i < values.size(); i++) {
                    sb.append(values.get(i));
                    if (i < values.size() - 1) {
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
                break;

            case "INSERT":
                sb.append("INSERT INTO ");
                sb.append(tables.get(0));

                sb.append(" (");
                for (int i = 0; i < columns.size(); i++) {
                    sb.append(columns.get(i));
                    if (i < columns.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(") VALUES (");

                for (int i = 0; i < values.size(); i++) {
                    sb.append(values.get(i));
                    if (i < values.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
                break;

            case "DELETE":
                sb.append("DELETE FROM ");
                sb.append(tables.get(0));

                if (!conditions.isEmpty()) {
                    sb.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        sb.append(conditions.get(i));
                        if (i < conditions.size() - 1) {
                            sb.append(" AND ");
                        }
                    }
                }
                break;

            default:
                throw new IllegalStateException("Invalid query type");
        }

        return sb.toString();
    }
}
