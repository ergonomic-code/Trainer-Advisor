create table exercise_programs
(
    exercise_id BIGINT REFERENCES exercises (id) ON UPDATE CASCADE ON DELETE CASCADE,
    program_id  BIGINT REFERENCES programs (id) ON UPDATE CASCADE ON DELETE CASCADE
);
