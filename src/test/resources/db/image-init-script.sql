DELETE
FROM exercise_steps
WHERE exercise_id NOT IN (1, 2, 3, 4, 5);
DELETE
FROM images
WHERE id NOT IN (SELECT image_id FROM exercise_steps);
