-- =====================================================================================================================

CREATE TABLE IF NOT EXISTS user_actions (
  event_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  weight DOUBLE PRECISION NOT NULL,
  action_date TIMESTAMP NOT NULL,
  PRIMARY KEY (event_id, user_id)
);

ALTER TABLE user_actions DROP CONSTRAINT IF EXISTS user_actions_weight_check;
ALTER TABLE user_actions ADD CONSTRAINT user_actions_weight_check CHECK (weight >= 0.0);

COMMENT ON TABLE user_actions IS 'Содержит информацию о весах действий пользователей по событиям';
COMMENT ON COLUMN user_actions.event_id IS 'Уникальный идентификатор события';
COMMENT ON COLUMN user_actions.user_id IS 'Уникальный идентификатор пользователя';
COMMENT ON COLUMN user_actions.weight IS 'Вес действия';
COMMENT ON COLUMN user_actions.action_date IS 'Дата и время последнего действия';

-- =====================================================================================================================

CREATE TABLE IF NOT EXISTS event_similarity (
  event_id_a BIGINT NOT NULL,
  event_id_b BIGINT NOT NULL,
  score DOUBLE PRECISION NOT NULL,
  action_date TIMESTAMP NOT NULL,
  PRIMARY KEY (event_id_a, event_id_b)
);

ALTER TABLE event_similarity DROP CONSTRAINT IF EXISTS event_similarity_score_check;
ALTER TABLE event_similarity ADD CONSTRAINT event_similarity_score_check CHECK (score >= 0.0);

COMMENT ON TABLE event_similarity IS 'Содержит информацию о сходствах событий';
COMMENT ON COLUMN event_similarity.event_id_a IS 'Уникальный идентификатор события A';
COMMENT ON COLUMN event_similarity.event_id_b IS 'Уникальный идентификатор события B';
COMMENT ON COLUMN event_similarity.score IS 'Значение рассчитанного сходства событий';
COMMENT ON COLUMN event_similarity.action_date IS 'Дата и время действия инициировавшее расчет сходства событий';