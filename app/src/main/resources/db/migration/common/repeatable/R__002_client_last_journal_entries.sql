CREATE VIEW client_last_journal_entries AS
(
SELECT DISTINCT ON (client_ref) *
FROM journal_entries
ORDER BY client_ref, date DESC
    )