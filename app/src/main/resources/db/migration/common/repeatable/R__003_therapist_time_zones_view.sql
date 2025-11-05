DROP VIEW IF EXISTS therapist_time_zones;
CREATE OR REPLACE VIEW therapist_time_zones AS
(
SELECT therapists.id,
       COALESCE((SELECT time_zone
                 FROM appointments
                 WHERE appointments.therapist_ref = therapists.id
                 ORDER BY appointments.created_at DESC
                 LIMIT 1),
                'Asia/Novosibirsk') as time_zone
FROM therapists
    );
