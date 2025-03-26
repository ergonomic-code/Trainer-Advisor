-- Недельная статистика
SELECT u.email,
       (max(pl.last_used) AT TIME ZONE 'UTC') AT TIME ZONE 'Asia/Novosibirsk' last_action,
       count(distinct cc.id)                                                  created_clients,
       count(distinct uc.id)                                                  updated_clients,
       count(distinct c.id)                                                   total_clients,
       count(distinct je.id)                                                  journal_entries,
       max(ttje.date)                                                         last_entry_date,
       count(distinct ttje.id)                                                total_journal_entries
FROM therapists t
         JOIN users u ON u.id = t.id
         JOIN persistent_logins pl ON u.email = pl.username
         LEFT JOIN clients cc ON (cc.therapist_ref = t.id AND cc.created_at > NOW() - '1 WEEK'::interval)
         LEFT JOIN clients uc ON (uc.therapist_ref = t.id AND uc.modified_at > NOW() - '1 WEEK'::interval)
         LEFT JOIN clients c ON (c.therapist_ref = t.id)
         LEFT JOIN journal_entries je ON (je.client_ref = c.id AND
                                          greatest(je.created_at, je.last_modified_at) > NOW() - '1 WEEK'::interval)
         LEFT JOIN journal_entries ttje ON (ttje.client_ref = c.id)
WHERE pl.last_used > NOW() - '1 WEEK'::interval
GROUP BY u.id, t.id
;