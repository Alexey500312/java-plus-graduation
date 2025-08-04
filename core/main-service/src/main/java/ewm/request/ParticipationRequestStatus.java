package ewm.request;

/**
 * Статус заявки на участие.
 */
public enum ParticipationRequestStatus {
    /**
     * В ожидании.
     */
    PENDING,

    /**
     * Подтверждена.
     */
    CONFIRMED,

    /**
     * Отменено.
     */
    CANCELED,

    /**
     * Отклонена.
     */
    REJECTED;
}
