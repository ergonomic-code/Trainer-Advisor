UPDATE clients
SET distribution_source =
        CASE
            WHEN length(distribution_source) > 0 THEN 'FRIEND_REFERRAL'
            END;

ALTER TABLE clients
    RENAME COLUMN distribution_source TO distribution_source_type;

ALTER TABLE clients
    ADD COLUMN distribution_source_comment VARCHAR;