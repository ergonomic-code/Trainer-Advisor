-- Недельная статистика
SELECT u.email,
       count(distinct cc.id) created_clients,
       count(distinct uc.id) updated_clients,
       count(distinct je.id) journal_entries
FROM therapists t
         JOIN users u ON u.id = t.id
         JOIN persistent_logins pl ON u.email = pl.username
         LEFT JOIN clients cc ON (cc.therapist_ref = t.id AND cc.created_at > NOW() - '1 WEEK'::interval)
         LEFT JOIN clients uc ON (uc.therapist_ref = t.id AND uc.modified_at > NOW() - '1 WEEK'::interval)
         LEFT JOIN clients c ON (c.therapist_ref = t.id)
         LEFT JOIN journal_entries je ON (je.client_ref = c.id AND
                                          greatest(je.created_at, je.last_modified_at) > NOW() - '1 WEEK'::interval)
WHERE pl.last_used > NOW() - '1 WEEK'::interval
GROUP BY u.id, t.id
;