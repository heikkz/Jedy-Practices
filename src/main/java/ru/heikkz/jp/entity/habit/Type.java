package ru.heikkz.jp.entity.habit;

/**
 * Типы задач
 */
public enum Type {

    /**
     * Вакцины
     */
    VACCINE(0),
    /**
     * Индикаторы
     */
    INDICATOR(1),
    /**
     * Практики
     */
    PRACTICE(2);

    /**
     * Приоритет
     */
    private final int orderId;

    Type(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
