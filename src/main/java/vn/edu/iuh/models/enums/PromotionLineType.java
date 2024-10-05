package vn.edu.iuh.models.enums;

public enum PromotionLineType {
    /**
     * Mua vé tặng vé
     * Buy one, get one free
     */
    BUY_PRODUCTS_GET_PRODUCTS,
    /**
     * Mua sản phẩm tặng sản phẩm
     */
    BUY_TICKETS_GET_TICKETS,
    /**
     * Mua sản phẩm tặng vé
     * Buy one, get one free
     */
    BUY_PRODUCTS_GET_TICKETS,
    /**
     * Mua vé tặng sản phẩm
     */
    BUY_TICKETS_GET_PRODUCTS,
    /**
     * Chiếc khấu
     */
    PRICE_DISCOUNT,
    /**
     * Giảm tiền trực tiếp
     */
    CASH_REBATE
}
