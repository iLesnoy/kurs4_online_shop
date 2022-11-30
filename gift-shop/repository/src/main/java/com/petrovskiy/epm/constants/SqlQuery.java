package com.petrovskiy.epm.constants;

public class SqlQuery{
    public static final String FIND_PRODUCTS_BY_SEARCH_PART =
            "SELECT c FROM Product c WHERE c.name LIKE %:searchPart% OR c.description LIKE %:searchPart%";
    public static final String FIND_PRODUCTS_BY_CATEGORY_NAMES_AND_SEARCH_PART_ANTON =
            "SELECT p FROM Product p " +
                    "JOIN p.categoryList cat " +
                    "WHERE cat.name IN :categoryNameList " +
                    "OR (p.name LIKE %:searchPart% OR p.description LIKE %:searchPart%)";
    public static final String FIND_MOST_POPULAR_PRODUCT =
            """
            SELECT t.id, t.name FROM category as t
            JOIN product_has_category as gst ON gst.category_id=t.id
            JOIN product_has_orders as gco ON gco.product_id= gst.product_id
            JOIN orders as o ON o.id = gco.orders_id AND o.users_id=
                (SELECT u.id FROM users as u
                JOIN orders as ord ON ord.users_id = u.id
            	GROUP BY u.id ORDER BY sum(ord.order_cost) DESC LIMIT 1)
            	GROUP BY t.id ORDER BY count(t.id) DESC LIMIT 1""";

    private SqlQuery() {
    }
}
