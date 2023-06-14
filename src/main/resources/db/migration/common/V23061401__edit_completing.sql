ALTER TABLE completing
    drop CONSTRAINT completing_questionnaire_id_fkey;

ALTER TABLE completing
    ADD CONSTRAINT completing_questionnaire_id_fkey
        FOREIGN KEY (questionnaire_id)
            REFERENCES questionnaires
                (id)
            ON DELETE CASCADE;